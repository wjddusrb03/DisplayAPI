# Admin Commands

All commands require `displayapi.admin` permission (default: op).

## Commands

| Command | Alias | Description |
|---------|-------|-------------|
| `/displayapi test <type>` | `/dapi test <type>` | Spawn a test display |
| `/displayapi list` | `/dapi list` | List all active displays and groups |
| `/displayapi remove <id>` | `/dapi remove <id>` | Remove a display or group by ID |
| `/displayapi removeall` | `/dapi removeall` | Remove all displays and groups |
| `/displayapi save` | `/dapi save` | Save persistent displays to file |
| `/displayapi reload` | `/dapi reload` | Reload config.yml |
| `/displayapi info` | `/dapi info` | Show plugin version and stats |

## Test Types

`/dapi test <type>` supports these types:

| Type | Description |
|------|-------------|
| `text` | Simple text hologram |
| `block` | Block display (diamond block) |
| `item` | Item display (diamond sword) |
| `popup` | Animated popup text |
| `group` | Display group (3 text lines) |
| `pulse` | Pulse animation |
| `spin` | Spinning block animation |
| `bounce` | Floating item animation |
| `shake` | Shaking text animation |
| `fadein` | Fade-in text animation |
| `leaderboard` | Sample leaderboard |
| `interactive` | Clickable display (left/right click) |
| `follow` | Display that follows you |

## Permissions

```yaml
displayapi.admin:
  description: DisplayAPI admin permission
  default: op
```
