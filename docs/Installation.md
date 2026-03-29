# Installation

## Requirements

- **Paper 1.21.4+** (or forks like Purpur)
- **Java 21+**
- (Optional) [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

## Server Installation

1. Download `DisplayAPI-1.0.0.jar` from [Releases](https://github.com/wjddusrb03/DisplayAPI/releases)
2. Place it in your server's `plugins/` folder
3. Restart the server
4. Verify with `/dapi info`

## For Plugin Developers

### Add as Dependency

Add to your `plugin.yml`:
```yaml
depend:
  - DisplayAPI
```

Or as a soft dependency (optional):
```yaml
softdepend:
  - DisplayAPI
```

### Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.wjddusrb03</groupId>
        <artifactId>DisplayAPI</artifactId>
        <version>v1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle (JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.wjddusrb03:DisplayAPI:v1.0.0'
}
```

## Configuration

`plugins/DisplayAPI/config.yml`:

```yaml
# Default view range multiplier (1.0 = normal)
default-view-range: 1.0

# Auto-save interval in ticks (6000 = 5 minutes)
auto-save-interval: 6000
```
