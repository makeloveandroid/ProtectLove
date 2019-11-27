package com.protect.love.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.protect.love.bean.OpenProtectUser;
import com.protect.love.bean.AlarmTask;
import com.protect.love.bean.AlarmTaskLog;

import com.protect.love.dao.OpenProtectUserDao;
import com.protect.love.dao.AlarmTaskDao;
import com.protect.love.dao.AlarmTaskLogDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig openProtectUserDaoConfig;
    private final DaoConfig alarmTaskDaoConfig;
    private final DaoConfig alarmTaskLogDaoConfig;

    private final OpenProtectUserDao openProtectUserDao;
    private final AlarmTaskDao alarmTaskDao;
    private final AlarmTaskLogDao alarmTaskLogDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        openProtectUserDaoConfig = daoConfigMap.get(OpenProtectUserDao.class).clone();
        openProtectUserDaoConfig.initIdentityScope(type);

        alarmTaskDaoConfig = daoConfigMap.get(AlarmTaskDao.class).clone();
        alarmTaskDaoConfig.initIdentityScope(type);

        alarmTaskLogDaoConfig = daoConfigMap.get(AlarmTaskLogDao.class).clone();
        alarmTaskLogDaoConfig.initIdentityScope(type);

        openProtectUserDao = new OpenProtectUserDao(openProtectUserDaoConfig, this);
        alarmTaskDao = new AlarmTaskDao(alarmTaskDaoConfig, this);
        alarmTaskLogDao = new AlarmTaskLogDao(alarmTaskLogDaoConfig, this);

        registerDao(OpenProtectUser.class, openProtectUserDao);
        registerDao(AlarmTask.class, alarmTaskDao);
        registerDao(AlarmTaskLog.class, alarmTaskLogDao);
    }
    
    public void clear() {
        openProtectUserDaoConfig.clearIdentityScope();
        alarmTaskDaoConfig.clearIdentityScope();
        alarmTaskLogDaoConfig.clearIdentityScope();
    }

    public OpenProtectUserDao getOpenProtectUserDao() {
        return openProtectUserDao;
    }

    public AlarmTaskDao getAlarmTaskDao() {
        return alarmTaskDao;
    }

    public AlarmTaskLogDao getAlarmTaskLogDao() {
        return alarmTaskLogDao;
    }

}
