package com.awo.sample.register.zk;

import com.awo.sample.common.URL;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: Create by awo
 * @date: 2020/6/26
 * @Discription:
 **/
public class CuratorZookpeerImpl implements ZookpeerClient {

    private final CuratorFramework client;

    private static final int timeOut = 1000;

    private static final int sessionTimeOut = 3000;



    private final Set<String> persistentExistNodePath = new ConcurrentSkipListSet<>();

    public CuratorZookpeerImpl(URL url) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url.getZkAddress())
                .retryPolicy(new RetryNTimes(1, 1000))
                .connectionTimeoutMs(timeOut)
                .sessionTimeoutMs(sessionTimeOut);
        this.client = builder.build();
        // 先不实现
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        this.client.start();
    }

    @Override
    public void create(String path, boolean ephemeral) {
        // 非临时节点,判断是否重复
        if (!ephemeral) {
            if (persistentExistNodePath.contains(path)) {
                return;
            }
            if (checkExist(path)) {
                return;
            }
        }
        int i = path.indexOf("/");
        if (i > 0) {
            create(path.substring(0, i), false);
        }
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
            persistentExistNodePath.add(path);
        }
    }

    private void createPersistent(String path) {
        try {
            client.create().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEphemeral(String path) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            delete(path);
            createEphemeral(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkExist(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void delete(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
