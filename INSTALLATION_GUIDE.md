# UltimateServer Plugin - Installation Guide

Complete step-by-step guide for setting up and installing the UltimateServer Plugin.

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Pre-Installation Setup](#pre-installation-setup)
3. [Installation Steps](#installation-steps)
4. [Configuration](#configuration)
5. [Verification](#verification)
6. [Troubleshooting](#troubleshooting)

## System Requirements

### Java Version
- **Minimum**: Java 11 LTS
- **Recommended**: Java 17+
- **Check your Java version**: `java -version`

### Server Software
- Spigot 1.16+
- Paper 1.16+ (Recommended)
- Bukkit compatible server

### System Resources
- **RAM**: Minimum 2GB, Recommended 4GB+
- **Disk Space**: 500MB free space
- **Internet**: Required for Supabase integration

### Operating Systems
- Linux (Recommended for servers)
- Windows 10/11
- macOS

## Pre-Installation Setup

### Step 1: Set Up Supabase Database

1. **Create Supabase Account**
   - Go to https://supabase.com
   - Sign up for free account
   - Verify your email

2. **Create New Project**
   - Click "New Project"
   - Enter project name
   - Set password
   - Select region closest to your location
   - Click "Create new project"
   - Wait for project to initialize (2-3 minutes)

3. **Create Database Tables**
   - Open Project
   - Go to "SQL Editor"
   - Click "New Query"
   - Copy and paste this SQL:

```sql
-- Create Players Table
CREATE TABLE IF NOT EXISTS players (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id text UNIQUE NOT NULL,
  player_name text NOT NULL,
  balance double precision DEFAULT 1000,
  created_at timestamp DEFAULT now()
);

-- Create Warnings Table
CREATE TABLE IF NOT EXISTS warnings (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id text NOT NULL,
  reason text NOT NULL,
  created_at timestamp DEFAULT now()
);

-- Create Claims Table
CREATE TABLE IF NOT EXISTS claims (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  claim_id text UNIQUE NOT NULL,
  player_id text NOT NULL,
  x1 integer NOT NULL,
  z1 integer NOT NULL,
  x2 integer NOT NULL,
  z2 integer NOT NULL,
  created_at timestamp DEFAULT now()
);
```

   - Click "Run"
   - Verify tables are created

4. **Get Your API Keys**
   - Go to "Settings" → "API"
   - Copy these values (you'll need them later):
     - **Project URL** (Project_ID.supabase.co)
     - **Anon Key** (public key)
     - **Service Role Key** (keep secret!)

5. **Enable Row Level Security (RLS)**
   - Go to "Authentication" → "Policies"
   - Click "Enable RLS" on each table
   - Skip policy creation (we'll use public access)

### Step 2: Prepare Your Server

1. **Update Java** (if needed)
   ```bash
   # Ubuntu/Debian
   sudo apt update && sudo apt install openjdk-17-jre-headless

   # Check Java version
   java -version
   ```

2. **Verify Spigot Installation**
   ```bash
   # Navigate to server directory
   cd /path/to/minecraft/server

   # Check if plugins folder exists
   ls -la plugins/
   ```

3. **Back Up Current Server**
   ```bash
   # Backup everything before adding plugins
   tar -czf server_backup_$(date +%Y%m%d).tar.gz *
   ```

## Installation Steps

### Step 1: Download Plugin

**Option A: Download Pre-Built JAR**
- Download from: https://github.com/yourusername/ultimate-server-plugin/releases
- Latest version: UltimateServer-1.0.0.jar

**Option B: Build from Source**
```bash
# Clone repository
git clone https://github.com/yourusername/ultimate-server-plugin.git
cd ultimate-server-plugin

# Build with Maven
mvn clean package

# JAR location: target/ultimate-server-plugin-1.0.0.jar
```

### Step 2: Copy Plugin to Server

```bash
# Copy JAR to plugins folder
cp ultimate-server-plugin-1.0.0.jar /path/to/minecraft/server/plugins/

# Verify
ls -la /path/to/minecraft/server/plugins/ | grep ultimate
```

### Step 3: Configure Plugin

1. **First Start** (plugin creates config)
   ```bash
   # Start server to generate config
   ./start.sh  # or java -jar spigot.jar

   # Wait for "ENABLED SUCCESSFULLY" message
   # Stop server: type 'stop'
   ```

2. **Edit Configuration File**
   ```bash
   # Open config
   nano plugins/UltimateServer/config.yml

   # Or use your preferred editor:
   # vim plugins/UltimateServer/config.yml
   ```

3. **Update with Your Credentials**
   ```yaml
   database:
     url: "https://YOUR_PROJECT_ID.supabase.co"
     api-key: "YOUR_SERVICE_ROLE_KEY"
     anon-key: "YOUR_ANON_KEY"
   ```

   Replace:
   - `YOUR_PROJECT_ID`: From Supabase
   - `YOUR_SERVICE_ROLE_KEY`: From Supabase Settings → API
   - `YOUR_ANON_KEY`: From Supabase Settings → API

4. **Save and Close**
   - Save file (Ctrl+X, then Y for Vim/Nano)

### Step 4: Start Server

```bash
# Start server
./start.sh

# Or with Java directly
java -Xmx4G -Xms2G -jar spigot.jar nogui

# Look for these messages:
# "Config loaded successfully!"
# "Database connection established!"
# "ENABLED SUCCESSFULLY!"
```

## Configuration

### Main Config File
File: `plugins/UltimateServer/config.yml`

```yaml
# Database Configuration
database:
  url: "https://PROJECT_ID.supabase.co"
  api-key: "SERVICE_ROLE_KEY"
  anon-key: "ANON_KEY"

# Performance Optimization
optimization:
  enabled: true
  check-interval: 20              # Ticks
  max-entities: 500              # Per chunk
  max-chunks: 1000               # Per world
  memory-threshold: 85            # Percentage
  async-tasks: true              # Enable async

# Economy System
economy:
  enabled: true
  starting-balance: 1000.0        # Initial balance
  max-balance: 9999999.0          # Maximum balance
  transaction-fee: 0.0            # Fee percentage

# Protection System
protection:
  enabled: true
  max-claim-size: 10000           # Blocks
  max-claims-per-player: 5        # Per player
  claim-price: 100.0              # Cost to claim

# Moderation System
moderation:
  enabled: true
  mute-duration: 3600             # Seconds
  warn-threshold: 5               # Warnings before kick
  auto-kick-warnings: 10          # Auto kick at warnings
  auto-ban-kicks: 5               # Auto ban at kicks
```

### Fine-Tuning Performance

**For Low-End Servers (2GB RAM)**
```yaml
optimization:
  max-entities: 250
  max-chunks: 500
  memory-threshold: 75
```

**For High-End Servers (8GB+ RAM)**
```yaml
optimization:
  max-entities: 1000
  max-chunks: 2000
  memory-threshold: 90
```

## Verification

### Step 1: Check Plugin Loaded

```bash
# In server console, type:
/admin status

# Expected output:
# === Server Status ===
# Online Players: 0
# TPS: 20.00
# Memory: 512.0MB
```

### Step 2: Test Commands

```bash
# In game as OP:
/admin stats                # Should show stats
/economy balance            # Should show your balance
/player info                # Should show your info
```

### Step 3: Check Logs

```bash
# View server logs
tail -f logs/latest.log | grep UltimateServer

# Should see:
# [INFO] UltimateServer Plugin v1.0.0 - Initializing...
# [INFO] Config loaded successfully!
# [INFO] Database connection established!
# [INFO] UltimateServer Plugin - ENABLED SUCCESSFULLY!
```

## Troubleshooting

### Plugin Not Loading

**Problem**: Plugin doesn't appear in `/plugins` command

**Solutions**:
1. Verify JAR is in `plugins/` folder
2. Check server console for errors
3. Verify Java 11+ is installed
4. Check file permissions: `chmod 644 plugins/*.jar`
5. Restart server

### Database Connection Error

**Problem**: "Database connection failed"

**Solutions**:
1. **Check Credentials**
   - Verify config.yml has correct URL and keys
   - Ensure no spaces or typos

2. **Test Connection**
   - Ping Supabase: `ping PROJECT_ID.supabase.co`
   - Check internet connection

3. **Firewall Issues**
   - Add firewall rule for HTTPS (port 443)
   - Test: `curl https://PROJECT_ID.supabase.co`

4. **Table Issues**
   - Verify tables exist in Supabase SQL Editor
   - Check table names are exact (lowercase)
   - Ensure RLS is enabled

### High Memory Usage

**Problem**: Server using too much RAM

**Solutions**:
1. Reduce entity limit:
   ```yaml
   optimization:
     max-entities: 200
   ```

2. Force garbage collection:
   ```
   /admin gc
   ```

3. Reduce chunk loading:
   ```yaml
   optimization:
     max-chunks: 500
   ```

### Commands Not Working

**Problem**: Commands return "Unknown command"

**Solutions**:
1. Check permissions in config
2. Verify command name (case-sensitive)
3. Ensure player has permission
4. Try `/admin help` to verify plugin loaded

### Low TPS/Lag

**Problem**: Server has low TPS (below 15)

**Solutions**:
1. **Check Status**
   ```
   /admin lag
   ```

2. **Reduce Load**
   ```yaml
   optimization:
     max-entities: 250
     max-chunks: 500
   ```

3. **Force GC**
   ```
   /admin gc
   ```

4. **Check Entities**
   ```
   /admin entities world
   ```

5. **Review Plugins**
   - Disable other heavy plugins
   - Check for errors in console

## Performance Tips

1. **Regular Maintenance**
   - Run `/admin gc` daily
   - Monitor `/admin stats`
   - Review `/admin memory`

2. **Optimal Settings**
   - Match memory-threshold to your RAM
   - Set max-entities based on player count
   - Use async tasks when possible

3. **Database Optimization**
   - Keep Supabase project close to server
   - Monitor API usage
   - Archive old data monthly

4. **Server Optimization**
   - Use Paper instead of Spigot
   - Enable server watchdog
   - Use view distance wisely
   - Disable unnecessary features

## Support

**For Issues**:
1. Check this guide first
2. Review server logs
3. Check GitHub Issues
4. Contact support@ultimate-server.com

**For Feature Requests**:
- Open GitHub Issue
- Include use case description
- Attach server specs

## Next Steps

1. ✅ Installation complete!
2. 📖 Read main README.md for command documentation
3. 🔧 Customize config.yml for your server
4. 👥 Add administrators to server
5. 🎮 Invite players and test features

## Quick Reference

| Command | Purpose |
|---------|---------|
| `/admin status` | Server status |
| `/admin stats` | Performance stats |
| `/admin help` | Show all commands |
| `/economy balance` | Check balance |
| `/protect info` | Region info |
| `/player info` | Player info |

---

**Last Updated**: March 2025
**Version**: 1.0.0
**Support**: https://github.com/yourusername/ultimate-server-plugin
