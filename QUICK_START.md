# Quick Start Guide

Get UltimateServer running in 5 minutes!

## 🚀 30-Second Setup

### 1. Download
- Download latest JAR from [Releases](https://github.com/yourusername/ultimate-server-plugin/releases)
- Copy to `plugins/` folder

### 2. Configure Supabase (2 minutes)
- Create free account: https://supabase.com
- Create project
- Run SQL tables (see INSTALLATION_GUIDE.md)
- Copy API keys

### 3. Configure Plugin
- Edit `plugins/UltimateServer/config.yml`
- Paste Supabase credentials
- Save and restart

### 4. Verify
- Server console: Look for "ENABLED SUCCESSFULLY!"
- In-game: Type `/admin status`
- Should show server status

Done! ✅

## 📋 System Requirements

- Java 11+ (Java 17+ recommended)
- Spigot/Paper 1.16+
- 2GB+ RAM
- Internet connection

## 🎮 First Commands to Try

```
/admin status       → Check server
/admin stats        → Performance
/economy balance    → Your balance
/player info        → Your info
/protect info       → Region info
```

## 📚 Full Documentation

- [Installation Guide](INSTALLATION_GUIDE.md) - Detailed setup
- [README.md](README.md) - Features & commands
- [Configuration](README.md#configuration) - Config options

## ⚠️ Common Issues

**Plugin not loading?**
- Check Java version: `java -version`
- Verify JAR in plugins/ folder
- Check console for errors

**Database error?**
- Check config.yml credentials
- Verify Supabase tables exist
- Test internet connection

**Commands don't work?**
- Check OP permissions
- Verify plugin loaded: `/plugins`
- Try `/admin help`

## 🆘 Need Help?

- Check [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md#troubleshooting)
- Open [GitHub Issue](https://github.com/yourusername/ultimate-server-plugin/issues)
- Read [README.md](README.md)

---

**Next**: Read [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md) for detailed setup
