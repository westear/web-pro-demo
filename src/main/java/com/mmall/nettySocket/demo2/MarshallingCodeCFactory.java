package com.mmall.nettySocket.demo2;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;


/**
 * Netty传递对象信息需要序列化编解码工具Marshalling工具类
 * 对jdk默认的序列化进行了优化，又保持跟java.io.Serializable接口的兼容，同时增加了一些可调的参数和附加特性，
 * 并且这些参数和特性可通过工厂类的配置
 * 1.可拔插的类解析器，提供更加便捷的类加载定制策略，通过一个接口即可实现定制。
 * 2.可拔插的对象替换技术，不需要通过继承的方式。
 * 3.可拔插的预定义类缓存表，可以减少序列化的字节数组长度，提升常用类型的对象序列化性能。
 * 4.无须实现java.io.Serializable接口
 * 5.通过缓存技术提升对象的序列化性能。
 * 6.使用非常简单
 *
 * =====其他的序列化工具有:==========
 *  google的Protobuf
 *  基于Protobuf的Kyro
 *  MessagePack框架
 *
 *
 * @author Qinyunchan
 * @date Created in 2:58 PM 2018/12/5
 */

public final class MarshallingCodeCFactory {

    /**
     * 创建Jboss Marshalling解码器MarshallingDecoder
     * @return MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder(){

        //首先通过Marshalling工具类的静态方法获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象。
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        //创建了MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        //根据marshallerFactory和configuration创建provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory,configuration);
        //构建Netty的MarshallingDecoder对象，俩个参数分别为provider和单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider,1024);
        return decoder;
    }

    /**
     * 创建Jboss Marshalling编码器MarshallingEncoder
     * @return MarshallingEncoder
     */

    public static MarshallingEncoder buildMarshallingEncoder(){
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        //构建Netty的MarshallingEncoder对象，MarshallingEncoder用于实现序列化接口的POJO对象序列化为二进制数组
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
}
