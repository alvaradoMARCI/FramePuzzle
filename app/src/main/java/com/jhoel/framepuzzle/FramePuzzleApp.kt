package com.jhoel.framepuzzle

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application FramePuzzle.
 *
 * Punto de entrada de Hilt. Toda inicialización a nivel de proceso
 * debería realizarse dentro de un Initializer (Startup) o en los métodos
 * onCreate de los ViewModels, no aquí.
 *
 * Regla de seguridad (sección 47):
 *  - No guardar tokens ni credenciales en código.
 *  - El token GitHub del agente vive en variables de entorno locales,
 *    nunca en el repositorio.
 */
@HiltAndroidApp
class FramePuzzleApp : Application()
