package vip.yeee.memoo.demo.distribute.lock.zookeeper.lock.simple;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/29 18:24
 */
public interface Lock {

    boolean lock() throws Exception;

    boolean unlock();

}
