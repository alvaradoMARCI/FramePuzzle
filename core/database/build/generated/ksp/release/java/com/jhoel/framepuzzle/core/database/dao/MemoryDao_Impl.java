package com.jhoel.framepuzzle.core.database.dao;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import java.lang.Class;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MemoryDao_Impl implements MemoryDao {
  private final RoomDatabase __db;

  public MemoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
