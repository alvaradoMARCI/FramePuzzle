package com.jhoel.framepuzzle.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.jhoel.framepuzzle.core.database.dao.AchievementDao;
import com.jhoel.framepuzzle.core.database.dao.AchievementDao_Impl;
import com.jhoel.framepuzzle.core.database.dao.AlbumDao;
import com.jhoel.framepuzzle.core.database.dao.AlbumDao_Impl;
import com.jhoel.framepuzzle.core.database.dao.MemoryDao;
import com.jhoel.framepuzzle.core.database.dao.MemoryDao_Impl;
import com.jhoel.framepuzzle.core.database.dao.PuzzleDao;
import com.jhoel.framepuzzle.core.database.dao.PuzzleDao_Impl;
import com.jhoel.framepuzzle.core.database.dao.UserDao;
import com.jhoel.framepuzzle.core.database.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FramePuzzleDatabase_Impl extends FramePuzzleDatabase {
  private volatile UserDao _userDao;

  private volatile MemoryDao _memoryDao;

  private volatile PuzzleDao _puzzleDao;

  private volatile AlbumDao _albumDao;

  private volatile AchievementDao _achievementDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `avatar` TEXT, `level` INTEGER NOT NULL, `xp` INTEGER NOT NULL, `createdDate` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memories` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `originalImage` TEXT NOT NULL, `editedImage` TEXT, `createdDate` INTEGER NOT NULL, `album_id` TEXT, `progress` REAL NOT NULL, `favorite` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memories_album_id` ON `memories` (`album_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `puzzles` (`id` TEXT NOT NULL, `memory_id` TEXT NOT NULL, `type` TEXT NOT NULL, `difficulty` TEXT NOT NULL, `pieces` INTEGER NOT NULL, `completed` INTEGER NOT NULL, `time_millis` INTEGER NOT NULL, `moves` INTEGER NOT NULL, `created_date` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_puzzles_memory_id` ON `puzzles` (`memory_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `albums` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `cover` TEXT, `type` TEXT NOT NULL, `created_date` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `achievements` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, `unlocked_date` INTEGER, `xp_reward` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3f319d5506de41f857e9ca93c922ccdd')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `memories`");
        db.execSQL("DROP TABLE IF EXISTS `puzzles`");
        db.execSQL("DROP TABLE IF EXISTS `albums`");
        db.execSQL("DROP TABLE IF EXISTS `achievements`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("avatar", new TableInfo.Column("avatar", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("xp", new TableInfo.Column("xp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.jhoel.framepuzzle.core.database.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsMemories = new HashMap<String, TableInfo.Column>(8);
        _columnsMemories.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("originalImage", new TableInfo.Column("originalImage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("editedImage", new TableInfo.Column("editedImage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("album_id", new TableInfo.Column("album_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("progress", new TableInfo.Column("progress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemories.put("favorite", new TableInfo.Column("favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMemories = new HashSet<TableInfo.Index>(1);
        _indicesMemories.add(new TableInfo.Index("index_memories_album_id", false, Arrays.asList("album_id"), Arrays.asList("ASC")));
        final TableInfo _infoMemories = new TableInfo("memories", _columnsMemories, _foreignKeysMemories, _indicesMemories);
        final TableInfo _existingMemories = TableInfo.read(db, "memories");
        if (!_infoMemories.equals(_existingMemories)) {
          return new RoomOpenHelper.ValidationResult(false, "memories(com.jhoel.framepuzzle.core.database.entity.MemoryEntity).\n"
                  + " Expected:\n" + _infoMemories + "\n"
                  + " Found:\n" + _existingMemories);
        }
        final HashMap<String, TableInfo.Column> _columnsPuzzles = new HashMap<String, TableInfo.Column>(9);
        _columnsPuzzles.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("memory_id", new TableInfo.Column("memory_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("pieces", new TableInfo.Column("pieces", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("time_millis", new TableInfo.Column("time_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("moves", new TableInfo.Column("moves", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPuzzles.put("created_date", new TableInfo.Column("created_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPuzzles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPuzzles = new HashSet<TableInfo.Index>(1);
        _indicesPuzzles.add(new TableInfo.Index("index_puzzles_memory_id", false, Arrays.asList("memory_id"), Arrays.asList("ASC")));
        final TableInfo _infoPuzzles = new TableInfo("puzzles", _columnsPuzzles, _foreignKeysPuzzles, _indicesPuzzles);
        final TableInfo _existingPuzzles = TableInfo.read(db, "puzzles");
        if (!_infoPuzzles.equals(_existingPuzzles)) {
          return new RoomOpenHelper.ValidationResult(false, "puzzles(com.jhoel.framepuzzle.core.database.entity.PuzzleEntity).\n"
                  + " Expected:\n" + _infoPuzzles + "\n"
                  + " Found:\n" + _existingPuzzles);
        }
        final HashMap<String, TableInfo.Column> _columnsAlbums = new HashMap<String, TableInfo.Column>(5);
        _columnsAlbums.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlbums.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlbums.put("cover", new TableInfo.Column("cover", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlbums.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlbums.put("created_date", new TableInfo.Column("created_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlbums = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlbums = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlbums = new TableInfo("albums", _columnsAlbums, _foreignKeysAlbums, _indicesAlbums);
        final TableInfo _existingAlbums = TableInfo.read(db, "albums");
        if (!_infoAlbums.equals(_existingAlbums)) {
          return new RoomOpenHelper.ValidationResult(false, "albums(com.jhoel.framepuzzle.core.database.entity.AlbumEntity).\n"
                  + " Expected:\n" + _infoAlbums + "\n"
                  + " Found:\n" + _existingAlbums);
        }
        final HashMap<String, TableInfo.Column> _columnsAchievements = new HashMap<String, TableInfo.Column>(6);
        _columnsAchievements.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("unlocked", new TableInfo.Column("unlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("unlocked_date", new TableInfo.Column("unlocked_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("xp_reward", new TableInfo.Column("xp_reward", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAchievements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAchievements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAchievements = new TableInfo("achievements", _columnsAchievements, _foreignKeysAchievements, _indicesAchievements);
        final TableInfo _existingAchievements = TableInfo.read(db, "achievements");
        if (!_infoAchievements.equals(_existingAchievements)) {
          return new RoomOpenHelper.ValidationResult(false, "achievements(com.jhoel.framepuzzle.core.database.entity.AchievementEntity).\n"
                  + " Expected:\n" + _infoAchievements + "\n"
                  + " Found:\n" + _existingAchievements);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3f319d5506de41f857e9ca93c922ccdd", "83e9e60f764899edd5aebc69025d468d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","memories","puzzles","albums","achievements");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `memories`");
      _db.execSQL("DELETE FROM `puzzles`");
      _db.execSQL("DELETE FROM `albums`");
      _db.execSQL("DELETE FROM `achievements`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MemoryDao.class, MemoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PuzzleDao.class, PuzzleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AlbumDao.class, AlbumDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AchievementDao.class, AchievementDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public MemoryDao memoryDao() {
    if (_memoryDao != null) {
      return _memoryDao;
    } else {
      synchronized(this) {
        if(_memoryDao == null) {
          _memoryDao = new MemoryDao_Impl(this);
        }
        return _memoryDao;
      }
    }
  }

  @Override
  public PuzzleDao puzzleDao() {
    if (_puzzleDao != null) {
      return _puzzleDao;
    } else {
      synchronized(this) {
        if(_puzzleDao == null) {
          _puzzleDao = new PuzzleDao_Impl(this);
        }
        return _puzzleDao;
      }
    }
  }

  @Override
  public AlbumDao albumDao() {
    if (_albumDao != null) {
      return _albumDao;
    } else {
      synchronized(this) {
        if(_albumDao == null) {
          _albumDao = new AlbumDao_Impl(this);
        }
        return _albumDao;
      }
    }
  }

  @Override
  public AchievementDao achievementDao() {
    if (_achievementDao != null) {
      return _achievementDao;
    } else {
      synchronized(this) {
        if(_achievementDao == null) {
          _achievementDao = new AchievementDao_Impl(this);
        }
        return _achievementDao;
      }
    }
  }
}
