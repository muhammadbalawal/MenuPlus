#!/usr/bin/env python3
"""
Android Studio Auto-Backup
Simple background backup service for Android Studio projects.
"""

import os
import sys
import shutil
import time
import subprocess
import signal
from datetime import datetime
from pathlib import Path

# Configuration
PROJECT_DIR = Path(__file__).parent.resolve()
CONFIG_FILE = PROJECT_DIR / 'backup_config.json'
BACKUP_DIR = PROJECT_DIR / 'AndroidStudioRevisionHistory'
PID_FILE = PROJECT_DIR / '.backup_pid'
DEVELOPER_FILE = PROJECT_DIR / '.developer_name'

def create_default_config():
    """Create default config if missing"""
    import json
    config = {
        "enabled": True,
        "backup_interval": 180,
        "exclude_extensions": [".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico"],
        "skip_dirs": ["build", ".gradle", ".kotlin", "AndroidStudioRevisionHistory", ".git", ".idea"]
    }
    with CONFIG_FILE.open('w') as f:
        json.dump(config, f, indent=2)
    return config

def load_config():
    """Load config"""
    import json
    if not CONFIG_FILE.exists():
        return create_default_config()
    with CONFIG_FILE.open('r') as f:
        return json.load(f)

def get_developer_name():
    """Prompt for developer name"""
    print("\n" + "="*50)
    print("  Android Studio Auto-Backup")
    print("="*50)
    print("\nWho is working on this project?")
    print("  1. Muhammad")
    print("  2. Malik")
    print()
    
    while True:
        choice = input("Enter your choice (1 or 2): ").strip()
        if choice == '1':
            developer = 'Muhammad'
            break
        elif choice == '2':
            developer = 'Malik'
            break
        else:
            print("Invalid choice. Please enter 1 or 2.")
    
    with DEVELOPER_FILE.open('w') as f:
        f.write(developer)
    
    print(f"\n✓ Developer set to: {developer}")
    print(f"  Backups will be saved to: AndroidStudioRevisionHistory/{developer}/")
    print()
    
    return developer

def is_running():
    """Check if service is running"""
    if not PID_FILE.exists():
        return False, None
    
    try:
        with PID_FILE.open('r') as f:
            pid = int(f.readline().strip())
        
        if sys.platform == 'win32':
            result = subprocess.run(['tasklist', '/FI', f'PID eq {pid}'], 
                                  capture_output=True, text=True, 
                                  creationflags=subprocess.CREATE_NO_WINDOW)
            running = 'python' in result.stdout.lower()
        else:
            os.kill(pid, 0)
            running = True
        
        return running, pid
    except:
        return False, None

def start_service():
    """Start backup service"""
    running, pid = is_running()
    
    if running:
        print(f"\n✓ Already running (PID {pid})\n")
        return
    
    # Get developer name (prompts if first time)
    developer = get_developer_name()
    
    print("\nStarting backup service...")
    
    if sys.platform == 'win32':
        # Windows
        python_exe = Path(sys.executable)
        pythonw_exe = python_exe.parent / 'pythonw.exe'
        if not pythonw_exe.exists():
            pythonw_exe = python_exe
        
        startupinfo = subprocess.STARTUPINFO()
        startupinfo.dwFlags |= subprocess.STARTF_USESHOWWINDOW
        startupinfo.wShowWindow = 0
        
        subprocess.Popen(
            [str(pythonw_exe), __file__, '--background'],
            cwd=str(PROJECT_DIR),
            startupinfo=startupinfo,
            creationflags=subprocess.CREATE_NEW_PROCESS_GROUP | subprocess.DETACHED_PROCESS
        )
    else:
        # macOS/Linux
        subprocess.Popen(
            [sys.executable, __file__, '--background'],
            cwd=str(PROJECT_DIR),
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
            stdin=subprocess.DEVNULL,
            preexec_fn=os.setpgrp
        )
    
    time.sleep(1)
    
    if PID_FILE.exists():
        config = load_config()
        print(f"✓ Started successfully!")
        print(f"  Developer: {developer}")
        print(f"  Interval: {config['backup_interval']//60} minutes")
        print(f"  Backup to: AndroidStudioRevisionHistory/{developer}/")
        print(f"\nCommands:")
        print(f"  python autobackup.py --status")
        print(f"  python autobackup.py --stop\n")
    else:
        print("✗ Failed to start\n")

def show_status():
    """Show status"""
    running, pid = is_running()
    
    if running:
        print(f"\n✓ Running (PID {pid})")
        if PID_FILE.exists():
            with PID_FILE.open('r') as f:
                lines = f.read().strip().split('\n')
                if len(lines) >= 2:
                    print(f"  Started: {lines[1]}")
                if len(lines) >= 3:
                    print(f"  Last backup: {lines[2]}")
        print()
    else:
        print(f"\n✗ Not running\n")

def stop_service():
    """Stop service"""
    running, pid = is_running()
    
    if not running:
        print(f"\n✓ Not running\n")
        if PID_FILE.exists():
            PID_FILE.unlink()
        return
    
    print(f"\nStopping service (PID {pid})...")
    
    try:
        if sys.platform == 'win32':
            subprocess.run(['taskkill', '/F', '/PID', str(pid)], 
                         capture_output=True, creationflags=subprocess.CREATE_NO_WINDOW)
        else:
            os.kill(pid, signal.SIGTERM)
        
        time.sleep(1)
        if PID_FILE.exists():
            PID_FILE.unlink()
        print("✓ Stopped\n")
    except Exception as e:
        print(f"✗ Failed: {e}\n")

def run_backup_loop():
    """Main backup loop"""
    # Read developer name from file
    if DEVELOPER_FILE.exists():
        with DEVELOPER_FILE.open('r') as f:
            developer = f.read().strip()
    else:
        # Shouldn't happen, but fallback
        developer = 'Unknown'
    
    developer_backup_dir = BACKUP_DIR / developer
    
    # Save PID
    with PID_FILE.open('w') as f:
        f.write(f"{os.getpid()}\n")
        f.write(f"{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write("Never\n")
    
    config = load_config()
    if not config['enabled']:
        return
    
    developer_backup_dir.mkdir(parents=True, exist_ok=True)
    
    # Track files
    tracked = {}
    
    def scan():
        files = {}
        for root, dirs, filenames in os.walk(PROJECT_DIR):
            dirs[:] = [d for d in dirs if d not in config['skip_dirs']]
            for fname in filenames:
                if fname in ['autobackup.py', 'backup_config.json', '.backup_pid', '.developer_name']:
                    continue
                if any(fname.endswith(e) for e in config['exclude_extensions']):
                    continue
                fpath = Path(root) / fname
                try:
                    files[str(fpath.relative_to(PROJECT_DIR))] = fpath.stat().st_mtime
                except:
                    pass
        return files
    
    def backup_changed():
        current = scan()
        timestamp = datetime.now().strftime('%Y-%m-%d_%H-%M-%S')
        modified = [p for p, mt in current.items() if p not in tracked or mt > tracked[p]]
        
        if modified:
            for rel_path in modified:
                src = PROJECT_DIR / rel_path
                name, ext = src.stem, src.suffix
                dest = developer_backup_dir / rel_path.replace(src.name, f"{name}_{timestamp}{ext}")
                try:
                    dest.parent.mkdir(parents=True, exist_ok=True)
                    shutil.copy2(src, dest)
                except:
                    pass
            
            # Update last backup time
            with PID_FILE.open('r') as f:
                lines = f.read().strip().split('\n')
            lines[2] = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            with PID_FILE.open('w') as f:
                f.write('\n'.join(lines))
        
        return current
    
    tracked = scan()
    interval = config['backup_interval']
    last_check = time.time()
    
    try:
        while PID_FILE.exists():
            time.sleep(10)
            if time.time() - last_check >= interval:
                tracked = backup_changed()
                last_check = time.time()
    except:
        pass
    finally:
        if PID_FILE.exists():
            PID_FILE.unlink()

def main():
    if '--status' in sys.argv:
        show_status()
    elif '--stop' in sys.argv:
        stop_service()
    elif '--background' in sys.argv:
        run_backup_loop()
    else:
        start_service()

if __name__ == "__main__":
    main()