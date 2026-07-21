# Arquitectura

## Módulos (14)
- app: MainActivity, Application, Koin, navegación
- core/domain: modelos y casos de uso (shared kernel)
- core/database: Room, entidades, DAOs
- core/storage: LocalStorageManager, DataStore
- core/security: crypto, PIN, biometría
- core/designsystem: tema, colores, tipografía
- core/utils: utilidades
- feature/{camera,editor,puzzle,library,profile,backup,transfer,settings}

## Dependencias
app → todo
feature/* → core/domain + core/designsystem
core/* → core/domain

## DI
Koin 3.5.6 - configuración manual en AppModule.kt
