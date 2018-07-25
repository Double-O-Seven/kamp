/* Copyright (C) 2012-2015 Zeex
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

#include <sampgdk/platform.h>

#include "logprintf.h"

#define _SAMPGDK_LOGPRINTF_BUFFER_SIZE 1024

#ifdef _MSC_VER
  #define vsnprintf vsprintf_s
#endif

/*
 * Gets called instead of the real logprintf when the library has not been
 * initialized yet. See the declaration of sampgdk_logprintf_impl.
 */
static void _sampgdk_logprintf_stub(const char *format, ...) {
  va_list va;

  va_start(va, format);
  vprintf(format, va);
  va_end(va);

  printf("\n");
}

logprintf_t sampgdk_logprintf_impl = &_sampgdk_logprintf_stub;

void sampgdk_do_vlogprintf(const char *format, va_list va) {
  char buffer[_SAMPGDK_LOGPRINTF_BUFFER_SIZE];

  vsnprintf(buffer, sizeof(buffer), format, va);
  buffer[sizeof(buffer) - 1] = '\0';

  sampgdk_logprintf_impl("%s", buffer);
}
