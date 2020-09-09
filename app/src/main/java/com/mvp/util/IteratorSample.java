package com.mvp.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 */
public class IteratorSample {

    void i(){//迭代器
        Collection books = new HashSet();
        books.add("计算机网络");
        books.add("数字信号处理");
        books.add("java语言程序设计");

        for(Object obj:books){
            String info = (String)obj;
            System.out.println(info);
            if(info.equals("数字信号处理")){
                //books.remove(info);不能通过迭代变量修改集合，否则引发异常
            }
        }
    }

    void ii(){//迭代器
        Collection books = new HashSet();
        books.add("计算机网络");
        books.add("数字信号处理");
        books.add("java语言程序设计");
        //生成迭代器
        Iterator it = books.iterator();
        int i=0;
        while(it.hasNext()){
            //next()返回的数据是Object型，需要强制转化
            String info = (String)it.next();
            //输出遍历的每一个元素
            System.out.println("第"+i+"个元素："+info);
            if(info.equals("数字信号处理")){
                System.out.println(i);
                it.remove(); //把《数字信号处理》从集合books中移除
            }
            i++;
        }
        //输出移除后的全部结果
        System.out.println(books);
    }
}
