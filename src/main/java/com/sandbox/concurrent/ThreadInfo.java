package com.sandbox.concurrent;

import static java.util.Objects.requireNonNull;

import java.lang.Thread.State;

public class ThreadInfo {

  private final String name;

  private final long id;

  private final Thread.State state;

  public ThreadInfo(long id, String name, State state) {
    this.id = id;
    this.name = requireNonNull(name);
    this.state = requireNonNull(state);
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public State getState() {
    return state;
  }
}
