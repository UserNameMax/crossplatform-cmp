# Грабли Compose Multiplatform

Только **воспроизведённое запуском**. Утверждение из статьи или из документации сюда не идёт.

**Замерено 22–23.08.2026** на: Gradle 9.1.0, Kotlin 2.4.10, AGP 9.0.1,
Compose Multiplatform 1.11.1, Material3 1.11.0-alpha07, Navigation 3 1.1.1.
Через полгода перепроверить по этой дате.

**Дополнено 24.08.2026**, ПЗ4 (`pokedex-cmp`, ветка `workshop/w4-*`): добавились Ktor 3.1.2,
kotlinx.serialization 1.7.3, Coil 3.5.0, `androidx.lifecycle:lifecycle-viewmodel-navigation3`.

Файл пополняется после каждого воркшопа на CMP — ПЗ5 14.11.

---

## Корутины и сеть (добавлено на ПЗ4)

**`catch (e: Exception)` не ловит сетевой сбой Ktor на JS/Wasm.** `Fail to fetch` из
браузерного `fetch()` приходит в Kotlin как `kotlin.Error`, а `Error` не наследует
`Exception` (оба — `Throwable`, но в разных ветках). Код, который "работал" на JVM/Android
(там сбои — `java.io.IOException`, настоящий `Exception`), на wasmJs тихо ронял корутину в
необработанное исключение. Ловить `catch (e: Throwable)`, и обязательно с явным
`catch (e: CancellationException) { throw e }` перед ним — иначе `Throwable` перехватит и
отмену тоже.

**`async {}` + последовательные `.await()` без `coroutineScope {}` маскируют реальную
причину сбоя.** Запустили `detailAsync` и `speciesAsync` параллельно, await-им по очереди:
если `speciesAsync` падает раньше, чем мы дошли до `detailAsync.await()`, — родитель
начинает отменяться, и `detailAsync.await()` (единственный вызов, до которого дошло
исполнение) бросает `CancellationException`. Настоящая ошибка `speciesAsync` при этом
никогда не была `.await()`-нута и улетает в глобальный необработанный обработчик отдельно —
на экране пользователь не увидит `ScreenState.Error`, а в консоли будет висеть
самостоятельное "Uncaught". Лечится оборачиванием пары `async` в `coroutineScope { }`: он
сам собирает детей и пробрасывает наверх первую настоящую причину, а не первую попавшуюся
отмену.

**`androidx.lifecycle:lifecycle-viewmodel-navigation3` версии 2.11.0 (стабильной) тянет
требование AGP 9.1.0+.** Проект на AGP 9.0.1 падает на этапе применения плагина
(`lifecycle-viewmodel-navigation3-android:2.11.0 requires Android Gradle plugin 9.1.0`), а
AGP 9.1.0 в свою очередь требует Gradle 9.3.1 — целая цепочка апгрейдов ради одной
зависимости. Версия `2.11.0-beta01` (та же линия, что и остальной `androidx.lifecycle` в
проекте) этого требования не несёт и собирается на AGP 9.0.1 без изменений.

**`ViewModelStoreNavEntryDecorator` не входит в `NavDisplay` по умолчанию.** Без явного
`entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator())`
`viewModel {}` внутри `entry<T> { }` резолвится на общий `LocalViewModelStoreOwner` хоста, а
не на запись бэкстека — ViewModel никогда не очищается при уходе с экрана, и вся идея
структурной отмены при навигации назад не работает вообще.

<callout>
**Открытый вопрос, не закрытый на ПЗ4.** Даже с декоратором `onCleared()` ни разу не
сработал при живом прогоне на wasmJs, несмотря на несколько уходов «назад» с экрана
детали — не удалось подтвердить, что per-entry `ViewModelStore` реально освобождается на
этом таргете (артефакт версии `2.11.0-beta01`, возможно незрелая реализация для
non-Android). Код соответствует официальной документации Google. **Проверить на Android
до 31.10** — если там `onCleared()` тоже не срабатывает, гвоздь Л4 нечем показать руками
на этой связке библиотек, и чекпойнт 4 придётся переделывать на ручное управление scope.
</callout>

**Добавление новых зависимостей, затрагивающих js/wasmJs, ломает `kotlinWasmStoreYarnLock`
без явного апдейта.** После добавления Ktor JS-движка и Coil сборка `wasmJsBrowserDevelopmentRun`
падала с `Lock file was changed. Run the kotlinWasmUpgradeYarnLock task`. Разово прогнать
`./gradlew kotlinWasmUpgradeYarnLock` перед первой сборкой web после смены зависимостей.

**`StandardTestDispatcher(testScheduler)`, подставленный в `Dispatchers.Main`, не двигает
корутины `viewModelScope` в этой версии `androidx.lifecycle` (2.11.0-beta01, KMP).** Ни
`testScheduler.advanceUntilIdle()`, ни `UnconfinedTestDispatcher` для Main не помогли —
состояние оставалось `Loading` до бесконечности. Рабочий обход: не управлять временем
вручную, а ждать результат через `viewModel.state.first { условие }` — сама подписка
корректно дожидается результата независимо от того, на каком диспетчере он в реальности
считается. `withTimeout()` при этом тоже не подходит: он считает **виртуальные** секунды
`runTest`, а не настоящие, и может сработать раньше, чем настоящая пауза (например,
`debounce()`) реально прошла.

---

## Сборка и зависимости

**UI-половина Navigation 3 у Google собрана не под наши таргеты.**
`androidx.navigation3:navigation3-ui` публикуется только под `androidJvm`, `jvm` и
`linux_x64` — ни wasm, ни js, ни iOS. В её `nonAndroidMain` лежат заглушки
`implementedInJetBrainsFork()`. Мультиплатформенную половину публикует JetBrains:
`org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1`, стабильная, на Maven Central,
таргеты `common / js / jvm / wasm / ios / macos`. Runtime берётся гугловский
(`androidx.navigation3:navigation3-runtime:1.1.1`) и тянется транзитивно.

> Navigation 3 при этом **не в альфе**: у `androidx.navigation3` стабильны `1.0.0` … `1.1.6`.
> Проблема именно в наборе таргетов, а не в зрелости.

**Три спецификации анимации Navigation 3 обязательны.** `transitionSpec`, `popTransitionSpec`
и `predictivePopTransitionSpec` надо задать явно: их значения по умолчанию на не-Android
таргетах — те самые заглушки, и приложение упадёт с `NotImplementedError` на первом переходе.

**Задать только `enterTransition` и `exitTransition` мало.** Обратное направление — это
`popEnterTransition` и `popExitTransition`; по умолчанию они повторяют прямое, и возврат
читается как ещё один шаг вперёд.

**`slideIntoContainer` — метод `AnimatedContentTransitionScope`, а не функция верхнего уровня.**
Импортировать нечего, `Unresolved reference` при попытке.

**Иконок Material в Compose Multiplatform 1.11 нет.** Стрелки и переключатели делаются
векторными ресурсами в `composeResources/drawable`. Для курса это даже лучше: ресурсы через три
таргета — отдельная тема вехи В3.

**`desktopApp` не видит `stringResource`** без явной зависимости на
`compose.components.resources`. Транзитивно из общего модуля она не приходит.

**Web-модуль не видит навигацию транзитивно** из общего модуля — нужна явная зависимость,
если в web-точке входа что-то из неё используется.

## Ресурсы

**Генерируемый `Res` объявлен `internal`** и не виден из модулей-точек входа. Заголовок окна
desktop ресурсом без этого не сделать. Лечится:

```kotlin
compose.resources {
    publicResClass = true
    packageOfResClass = "<пакет>.resources"
}
```

Заодно уходит уродливый `<rootProject>.shared.generated.resources`.

**В ресурсах работают только позиционные `%1$s`.** `%.1f` уезжает на экран буквально.
Форматировать число надо в коде и подставлять строкой.

**На web ресурсы грузятся асинхронно, около полусекунды.** Первый кадр приходит без шапки,
без картинок и без переводов. Это не баг — это тема вехи В3, и на паре стоит показать.

**Паритет локалей глазами не ловится.** Ключ, забытый в одной локали, при переключении языка
молча оставит на экране чужой язык. Нужен скрипт с ненулевым кодом возврата — и проверить его
в обе стороны, а не только на зелёном случае.

**Плагин Compose затирает `-Duser.language`.** Он подставляет свой уже после разбора скрипта,
так что заданный в `jvmArgs` не доживает. Дописывать в `doFirst` — у JVM выигрывает последний
`-D` с тем же ключом:

```kotlin
tasks.withType<JavaExec>().configureEach {
    if (name != "run") return@configureEach
    providers.gradleProperty("locale").orNull?.let { locale ->
        doFirst { jvmArgs("-Duser.language=$locale", "-Duser.country=") }
    }
}
```

Задача `run` создаётся плагином после разбора скрипта, поэтому `withType`, а не по имени.

## Compose-компилятор и рекомпозиция

**`composable` не пропускается внутрь лямбды `joinToString`** — она не inline. Сначала
`map` (inline), потом склейка.

**Класс состояния с полем-списком выводится `Uncertain`, но пропуск это не отменяет.**
Проверено отчётом компилятора: класс состояния помечен `Uncertain(List)`, а **все экраны
остались `restartable skippable`** — стабильность параметра решается на рантайме.

Пропуск ломает другое: **передача в composable объекта, выведенного как `Unstable`**. Такова
ViewModel. Отдать её в экран целиком — экран перестанет быть skippable. Поэтому вниз идут
состояние и ссылка на метод, которую отчёт помечает `stable`.

Отчёты включаются так и стоят того — это же артефакт для ПЗ8 22.12:

```kotlin
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}
```

**Чего отчёт не показывает:** пропускают ли функции перерисовку в действительности. Он
статический; для рантайма нужен счётчик рекомпозиций, и его место — в демо-стенде, а не в
шаблоне студентов.

**Область рекомпозиции решает, сработает ли демо вообще.** Значение должно вычисляться в той
же области, в которой читается состояние. Иначе тело не перевызовется, и «сломанный» вариант
поведёт себя как починенный.

## Git и окружение

**`gradlew.bat` уезжает в LF** и не запустится у студентов на Windows. Нужен `.gitattributes`
с `*.bat text eol=crlf`.

**`kotlin-js-store/` появляется после первой сборки web** и оставляет дерево грязным ровно
перед шагом «коммит и PR». В `.gitignore`.

**`git checkout <тег>` оставляет в detached HEAD**, и первый же коммит студента окажется ни в
одной ветке. Git предупреждает одной строкой, которую на паре никто не читает. Стартовать
через `git switch -c <своя-ветка> <тег>`.

## Данные PokéAPI

**`Python-urllib` получает 403 от Cloudflare.** `Ktor client`, `okhttp` и `curl` проходят —
курсу это не мешает, скриптам подготовки данных мешает.

**В текстах стоят мягкие переносы.** После схлопывания пробелов получается «scav enges» и
«whis kers». Снимать надо вместе с идущим следом пробелом: `re.sub('­\\s*', '', text)`.

## Проверка UI без экрана

Скриншот десктопного окна из-под агента снять нельзя — нет прав на запись экрана,
`screencapture` возвращает «could not create image from display».

Рабочий обход: поднять web-таргет и смотреть через браузер. Заодно проверяется web.

```bash
nohup ./gradlew :webApp:wasmJsBrowserDevelopmentRun --console=plain > /tmp/web-run.log 2>&1 &
# ждать, пока curl не вернёт 200 на http://localhost:8080/ — первый запуск до трёх минут
```

**Первый скриншот после действия почти всегда ловит недорисованный кадр** — делать второй.

## Персистентность и платформа (ПЗ5, проверено 25.08.2026)

Kotlin 2.4.10 / Compose Multiplatform 1.11.1. Все воспроизведены при сборке шаблона.

| Симптом | Причина |
|---|---|
| `androidx.datastore` не резолвится в `commonMain` | 1.2.0 публикует артефакт под `wasmJs`, но **не под `js`**. Проект курса объявляет оба web-таргета. В опубликованном wasm-варианте фабрика к тому же `TODO("Not yet implemented")` |
| `This declaration needs opt-in … ExperimentalWasmJsInterop` | любой `js("…")` в Kotlin/Wasm требует `@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)` |
| `… ExperimentalForeignApi` на iOS | то же для Foundation-API: `NSSearchPathForDirectoriesInDomains`, `writeToFile` |
| `'val maxWidth: Dp' cannot be called in this context with an implicit receiver` | `maxWidth` — свойство scope `BoxWithConstraints`. Вложили `Column` — неявный получатель потерян. Снимать ширину в локальную переменную сразу |
| Вторая панель показывает данные прошлой записи | ViewModel переиспользована между разными `id`. `key(id) { … }` |
| «Назад» браузера меняет адрес, экран прежний | написана только половина связи стек→адрес. Вторая половина — слушатель `popstate` |
| Переход зациклился, адрес мигает | петля стек→адрес→стек. Сравнение «уже такой же — ничего не делаем» в обеих половинах |
| Битый адрес `#/record/2/x/3` открыл 2 и 3 | `mapNotNull` склеил стек, потеряв середину. `takeWhile` обрезает |
| `Cannot access 'object Res': it is internal in file` из точки входа desktop | сгенерированный `Res` внутренний для своего модуля. Отдавать строку наружу composable-функцией из общего кода |
| `ImageComposeScene`: `Method setCurrentState must be called on the main thread` | сцену и `render()` трогать только с потока событий (`SwingUtilities.invokeAndWait`) |
| Headless-рендер выдал пустой экран | пауза между кадрами сделана **на** потоке событий — ресурсы и сеть не успели приехать. Спать на главном, рендерить на EDT |

### Чистый клон

Проверка «команды из README работают из чистого клона» поймала то, чего не видно
на своей машине: **desktop и web собираются сразу, Android падает с
`SDK location not found`**. `local.properties` лежит в `.gitignore` сознательно, но
README об этом молчал. Лечится строкой `echo "sdk.dir=$HOME/Library/Android/sdk" >
local.properties` или открытием в Android Studio.

Урок метода: правка README **стартового** состояния пересобирает всю цепочку тегов.
Здесь это стоило одного `git rebase --onto` и повторного прогона семи узлов. Дешевле,
чем кажется, но только пока цепочка своя и никто по ней ещё не работал.

### Что оказалось НЕ так, как ожидалось

- **`localStorage` через `js("…")` работает.** Ожидалась поломка на web, ломать оказалось
  нечего: сгенерированная таблица импортов wasm-бандла содержит все три моста дословно —
  проверено `grep` по `webApp.js`
- **Лямбда в `js("…")` на Kotlin/Wasm компилируется и работает.**
  `addPopStateListener(listener: () -> Unit)` даёт корректный мост
  `(listener) => { window.addEventListener('popstate', function () { listener(); }) }`
- **Пропущенный `actual` — ошибка компиляции, а не линковки.** Указывает на строку с `expect`
  в общем коде и роняет всю сборку, а не один таргет
