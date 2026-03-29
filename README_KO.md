# DisplayAPI

**Paper 1.21.4+용 경량 Display Entity API**

Display 엔티티(TextDisplay, BlockDisplay, ItemDisplay)를 쉽게 생성하고 관리할 수 있는 개발자 프레임워크입니다. 외부 의존성이 전혀 없습니다.

[English README](README.md)

---

## 주요 기능

- **플루언트 빌더 API** — 직관적인 메서드 체이닝으로 텍스트, 블록, 아이템 디스플레이를 한 줄로 생성
- **애니메이션 엔진** — 8종 프리셋 애니메이션 (펄스, 회전, 바운스, 떠다니기, 페이드, 흔들림...) + 12종 이징 함수를 지원하는 커스텀 키프레임
- **클릭 감지** — Interaction 엔티티와 결합하여 좌클릭/우클릭 처리
- **엔티티 추적** — 플레이어나 몹 위에 디스플레이를 부드럽게 부착
- **플레이어별 가시성** — 개별 플레이어에게 디스플레이를 표시하거나 숨김
- **디스플레이 그룹** — 여러 디스플레이를 하나의 단위로 관리 (일괄 이동, 제거)
- **리더보드** — 동적 데이터로 자동 갱신되는 순위 디스플레이
- **팝업 시스템** — 데미지 숫자, 경험치 알림 등을 위한 상승 + 페이드 텍스트
- **영속성** — YAML로 저장하여 서버 재시작 후에도 유지
- **PlaceholderAPI** — 동적 플레이스홀더 텍스트를 위한 선택적 연동
- **외부 의존성 제로** — Paper API만 필요, 다른 것은 불필요

## 빠른 시작

### 플러그인 개발자용

`plugin.yml`에 의존성 추가:
```yaml
depend:
  - DisplayAPI
```

### 기본 사용법

```java
// 텍스트 홀로그램
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("안녕하세요!").color(NamedTextColor.GOLD))
    .billboard(Billboard.CENTER)
    .shadowed(true)
    .noBackground()
    .spawn();

// 블록 디스플레이
DisplayAPI.block(location)
    .block(Material.DIAMOND_BLOCK)
    .scale(0.5f)
    .glow(Color.AQUA)
    .spawn();

// 아이템 디스플레이
DisplayAPI.item(location)
    .item(new ItemStack(Material.DIAMOND_SWORD))
    .billboard(Billboard.CENTER)
    .spawn();
```

### 데미지 팝업

```java
DisplayAPI.popup(entity.getLocation().add(0, 2, 0))
    .text(Component.text("-25").color(NamedTextColor.RED))
    .duration(30)            // 지속 시간 (틱)
    .startScale(1.5f)        // 시작 크기
    .endScale(0.3f)          // 끝 크기
    .visibleTo(attacker)     // 공격자에게만 보임
    .spawn();
// 자동으로 위로 올라가며 작아지고 투명해지면서 사라짐
```

### 애니메이션

```java
// 프리셋 애니메이션 (8종)
DisplayAPI.animate(display).pulse(0.8f, 1.5f, 30).loop(true).play();   // 크기 맥동
DisplayAPI.animate(display).spin(Axis.Y, 40).loop(true).play();        // 회전
DisplayAPI.animate(display).floating(0.3f, 40).loop(true).play();      // 둥둥 떠다니기
DisplayAPI.animate(display).shake(0.15f, 20).play();                    // 흔들림
DisplayAPI.animate(display).fadeIn(20).play();                          // 페이드 인
DisplayAPI.animate(display).fadeOut(20).play();                         // 페이드 아웃
DisplayAPI.animate(display).growIn(1.0f, 20).play();                   // 커지며 등장
DisplayAPI.animate(display).bounce(0.3f, 20).loop(true).play();       // 바운스

// 커스텀 키프레임 애니메이션
DisplayAPI.animate(display)
    .keyframe(Keyframe.at(0).scale(1f).translation(0, 0, 0))
    .keyframe(Keyframe.at(20).scale(2f).translation(0, 1f, 0))
    .keyframe(Keyframe.at(40).scale(1f).translation(0, 0, 0))
    .easing(Easing.EASE_IN_OUT)
    .loop(true)
    .play();
```

**이징 함수 12종:** LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, CUBIC 3종, QUART 3종, BACK 2종, BOUNCE, ELASTIC

### 클릭 가능한 디스플레이

```java
DisplayAPI.interactive(location)
    .text(Component.text("클릭하세요!").color(NamedTextColor.GREEN))
    .hitbox(1.5f, 1.0f)           // 히트박스 크기
    .onClick(player -> player.sendMessage("좌클릭!"))
    .onRightClick(player -> player.sendMessage("우클릭!"))
    .cooldown(200)                 // 쿨다운 (밀리초)
    .spawn();
```

### 엔티티 추적

```java
// 플레이어 머리 위를 따라다니는 디스플레이
DisplayAPI.follow(display, player)
    .offset(0, 2.5, 0)            // 머리 위 오프셋
    .smoothTeleport(3)             // 부드러운 이동
    .start();
```

### 디스플레이 그룹

```java
DisplayGroup group = DisplayAPI.group("my-group", anchorLocation);
group.add(display1);
group.add(display2);
group.add(display3);

group.teleport(newLocation);  // 전체 일괄 이동
group.remove();               // 전체 제거
```

### 리더보드

```java
// 정적 데이터
DisplayAPI.leaderboard(location)
    .title("킬 랭킹")
    .entry("Player_A", 150)
    .entry("Player_B", 120)
    .maxRows(10)
    .spawn();

// 동적 데이터 (자동 갱신)
DisplayAPI.leaderboard(location)
    .title("킬 랭킹")
    .dataSupplier(() -> getKillStats())
    .updateInterval(100)           // 갱신 주기 (틱)
    .spawn();
```

### 플레이어별 가시성

```java
DisplayAPI.text(location)
    .text(Component.text("나만 보이는 텍스트"))
    .visibleTo(player)
    .spawn();
```

### 영속성 (서버 재시작 후에도 유지)

```java
DisplayAPI.text(location)
    .text(Component.text("영구 홀로그램"))
    .persistent(true)
    .id("permanent-hologram")
    .spawn();
```

### 런타임 업데이트

```java
SpawnedDisplay display = DisplayAPI.getById("my-display");

display.updateText(Component.text("변경된 텍스트"));
display.updateBlock(Material.GOLD_BLOCK);
display.updateItem(new ItemStack(Material.BOW));
display.setGlowing(true);
display.setGlowColor(Color.RED);
display.setBillboard(Billboard.FIXED);
display.smoothTeleport(newLocation, 5);   // 부드러운 텔레포트
```

### PlaceholderAPI 연동

```java
import com.wjddusrb03.displayapi.util.PlaceholderUtil;

// 플레이어의 플레이스홀더 파싱
String parsed = PlaceholderUtil.parse(player, "체력: %player_health%");
Component comp = PlaceholderUtil.parse(player, component);
```

## 공통 속성 (모든 빌더)

| 메서드 | 설명 |
|--------|------|
| `.billboard(Billboard)` | CENTER, FIXED, HORIZONTAL, VERTICAL |
| `.viewRange(float)` | 렌더링 거리 배율 |
| `.scale(float)` | 균일 크기 |
| `.scale(x, y, z)` | 비균일 크기 |
| `.translation(x, y, z)` | 위치 오프셋 |
| `.glow(Color)` | 발광 외곽선 (색상 지정) |
| `.brightness(block, sky)` | 밝기 고정 |
| `.shadow(radius, strength)` | 그림자 설정 |
| `.visibleTo(Player...)` | 특정 플레이어만 보이게 |
| `.persistent(boolean)` | 서버 재시작 후에도 유지 |
| `.id(String)` | 고유 식별자 |

## 관리자 명령어

| 명령어 | 설명 |
|--------|------|
| `/dapi create text <텍스트> [id:이름]` | 텍스트 디스플레이 생성 (&색코드 지원) |
| `/dapi create block <블록> [id]` | 블록 디스플레이 생성 |
| `/dapi create item <아이템> [id]` | 아이템 디스플레이 생성 |
| `/dapi edit <id> <속성> <값>` | 디스플레이 수정 (text, block, item, scale, billboard, glow) |
| `/dapi move <id>` | 디스플레이를 현재 위치로 이동 |
| `/dapi animate <id> <종류>` | 애니메이션 적용 (pulse, spin, bounce, float, shake, fadein, fadeout, growin) |
| `/dapi popup <텍스트>` | 팝업 텍스트 생성 |
| `/dapi near [반경]` | 근처 디스플레이 검색 |
| `/dapi remove <id>` | ID로 제거 |
| `/dapi removeall` | 모든 디스플레이 제거 |
| `/dapi list` | 활성 디스플레이 목록 |
| `/dapi save` | 영속 디스플레이 저장 |
| `/dapi reload` | 설정 재로드 |
| `/dapi info` | 플러그인 정보 |

권한: `displayapi.admin` (기본: op)

## 요구사항

- Paper 1.21.4+
- Java 21+
- (선택) PlaceholderAPI

## 설치 방법

1. [Releases](https://github.com/wjddusrb03/DisplayAPI/releases)에서 `DisplayAPI-1.0.0.jar` 다운로드
2. 서버의 `plugins/` 폴더에 넣기
3. 서버 재시작

## 라이선스

MIT License
