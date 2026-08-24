# Демо-стенд Л5 — взаимодействие с платформой

Веха **В3**, лекция **Л5** (CMP), 10.11, онлайн. Курс кроссплатформенной разработки, ПИН-252т.

Стенд заводится один на веху: `v1` — В1, `v2` — В2, `v3` — В3.

## Запуск

```bash
./gradlew :desktopApp:run                        # ведущий таргет
./gradlew :webApp:wasmJsBrowserDevelopmentRun    # половина демо интересна только здесь
./gradlew :androidApp:assembleDebug
```

Снимок стенда без окна — страховка к паре и прогон демо руками:

```bash
./gradlew :desktopApp:renderCheck --args="1200 binding"
```

Кладёт PNG в `desktopApp/build/render/`. Без второго аргумента снимает список демо;
с ним — конкретное демо по его идентификатору.

## Демо

| Уровень | Демо | Идентификатор |
|---|---|---|
| Как пользоваться | expect/actual: один код, три ответа | `expect-actual` |
| Как пользоваться | Ресурсы через три таргета | `resources` |
| Как пользоваться | Адаптив: решает ширина, не устройство | `adaptive` |
| Как устроено | Стек объектов и адрес страницы | `stack-address` |
| Как устроено | Что считается источником правды | `source-of-truth` |
| Под капотом | Во что превратился expect — **гвоздь пары** | `binding` |

Сценарий каждого демо, критерии приёмки и замеры — в [SPEC.md](SPEC.md).

## Дампы сборки

`shared/src/commonMain/composeResources/files/binding-dumps.txt` — не пересказ, а вывод
инструментов: `javap`, `nm`, сгенерированный `Shared.h`, таблица импортов wasm-бандла и
текст ошибки компиляции из удалённого `actual`. Сняты 25.08.2026 на этом же проекте,
команды записаны рядом с выводом.

Пересобрать их можно так:

```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
nm -gU shared/build/bin/iosSimulatorArm64/debugFramework/Shared.framework/Shared | grep PlatformFacts
./gradlew :webApp:wasmJsBrowserDevelopmentExecutableDistribution
grep -o "'band\.effective[^']*' : [^,]*" webApp/build/dist/wasmJs/developmentExecutable/webApp.js
```

## Правила стенда

Те же, что предъявляются студентам. **Строк в коде нет** — включая заголовок окна desktop:
он приходит из `windowTitle()`, который читает ресурс. Демо не пересобираются на паре:
всё, что переключается, переключается тумблером.
