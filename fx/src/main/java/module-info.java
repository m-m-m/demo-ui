/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides the implementation of the UI framework based on JavaFx.
 */
module io.github.mmm.demo.ui.fx {

  requires transitive io.github.mmm.ui.fx.all;

  requires transitive io.github.mmm.ui.fx.core;

  requires transitive io.github.mmm.demo.ui.shared;

  // TODO JMPS / Eclipse is buggy
  requires transitive io.github.mmm.ui;

  exports io.github.mmm.demo.ui.fx;

}