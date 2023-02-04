# 测试multiGet、pipeline、循环get性能
代码：
```java
public static<T> List<T> multiGet(Collection<String> keyList){
        return (List<T>)redisTemplate.opsForValue().multiGet(keyList);
    }

public static<T> List<T> multiGetUsePipeline(Collection<String> keyList){
    return (List<T>)redisTemplate.executePipelined((RedisCallback<Object>) redisConnection->{
        keyList.forEach(item->redisConnection.stringCommands().get(item.getBytes()));
        //keyList.forEach(item->redisConnection.get(item.getBytes()));
        return null;
    });
}
```
测试方法：testMultiGetAndPipeline()
## 本地redis5.0.10 for windows
```
key数量:10
轮次:0,multiGet用时:16279 us,pipeline用时:33472 us,forEach用时:5240 us 
轮次:1,multiGet用时:875 us,pipeline用时:5789 us,forEach用时:3728 us 
轮次:2,multiGet用时:893 us,pipeline用时:5064 us,forEach用时:3486 us 
轮次:3,multiGet用时:693 us,pipeline用时:4817 us,forEach用时:3276 us 
轮次:4,multiGet用时:648 us,pipeline用时:4783 us,forEach用时:3056 us 
轮次:5,multiGet用时:622 us,pipeline用时:4760 us,forEach用时:3166 us 
轮次:6,multiGet用时:832 us,pipeline用时:4432 us,forEach用时:2922 us 
轮次:7,multiGet用时:737 us,pipeline用时:4560 us,forEach用时:2860 us 
轮次:8,multiGet用时:519 us,pipeline用时:4552 us,forEach用时:2836 us 
轮次:9,multiGet用时:574 us,pipeline用时:4202 us,forEach用时:2576 us 

key数量:20
轮次:0,multiGet用时:1142 us,pipeline用时:3885 us,forEach用时:5252 us 
轮次:1,multiGet用时:671 us,pipeline用时:3930 us,forEach用时:5489 us 
轮次:2,multiGet用时:686 us,pipeline用时:3462 us,forEach用时:5068 us 
轮次:3,multiGet用时:621 us,pipeline用时:3607 us,forEach用时:4774 us 
轮次:4,multiGet用时:590 us,pipeline用时:3107 us,forEach用时:4959 us 
轮次:5,multiGet用时:575 us,pipeline用时:3256 us,forEach用时:4315 us 
轮次:6,multiGet用时:530 us,pipeline用时:3253 us,forEach用时:4424 us 
轮次:7,multiGet用时:567 us,pipeline用时:3144 us,forEach用时:4614 us 
轮次:8,multiGet用时:502 us,pipeline用时:3035 us,forEach用时:4270 us 
轮次:9,multiGet用时:487 us,pipeline用时:2797 us,forEach用时:4080 us 

key数量:50
轮次:0,multiGet用时:3899 us,pipeline用时:5312 us,forEach用时:11227 us 
轮次:1,multiGet用时:672 us,pipeline用时:5150 us,forEach用时:11172 us 
轮次:2,multiGet用时:567 us,pipeline用时:4923 us,forEach用时:10332 us 
轮次:3,multiGet用时:543 us,pipeline用时:4649 us,forEach用时:9716 us 
轮次:4,multiGet用时:764 us,pipeline用时:3922 us,forEach用时:9551 us 
轮次:5,multiGet用时:542 us,pipeline用时:4546 us,forEach用时:8983 us 
轮次:6,multiGet用时:568 us,pipeline用时:3859 us,forEach用时:8775 us 
轮次:7,multiGet用时:423 us,pipeline用时:3158 us,forEach用时:8611 us 
轮次:8,multiGet用时:440 us,pipeline用时:3496 us,forEach用时:8680 us 
轮次:9,multiGet用时:481 us,pipeline用时:3618 us,forEach用时:8880 us 

key数量:100
轮次:0,multiGet用时:935 us,pipeline用时:4587 us,forEach用时:13038 us 
轮次:1,multiGet用时:636 us,pipeline用时:4275 us,forEach用时:14633 us 
轮次:2,multiGet用时:665 us,pipeline用时:5417 us,forEach用时:15579 us 
轮次:3,multiGet用时:600 us,pipeline用时:5042 us,forEach用时:15398 us 
轮次:4,multiGet用时:892 us,pipeline用时:5891 us,forEach用时:16745 us 
轮次:5,multiGet用时:560 us,pipeline用时:5463 us,forEach用时:16227 us 
轮次:6,multiGet用时:680 us,pipeline用时:5140 us,forEach用时:16090 us 
轮次:7,multiGet用时:633 us,pipeline用时:6520 us,forEach用时:18141 us 
轮次:8,multiGet用时:626 us,pipeline用时:6449 us,forEach用时:17774 us 
轮次:9,multiGet用时:578 us,pipeline用时:5513 us,forEach用时:18757 us 

key数量:200
轮次:0,multiGet用时:1126 us,pipeline用时:8650 us,forEach用时:26677 us 
轮次:1,multiGet用时:833 us,pipeline用时:7583 us,forEach用时:27993 us 
轮次:2,multiGet用时:896 us,pipeline用时:8497 us,forEach用时:26325 us 
轮次:3,multiGet用时:696 us,pipeline用时:7354 us,forEach用时:26587 us 
轮次:4,multiGet用时:702 us,pipeline用时:8108 us,forEach用时:26823 us 
轮次:5,multiGet用时:820 us,pipeline用时:6287 us,forEach用时:28399 us 
轮次:6,multiGet用时:754 us,pipeline用时:6946 us,forEach用时:29385 us 
轮次:7,multiGet用时:874 us,pipeline用时:7197 us,forEach用时:28684 us 
轮次:8,multiGet用时:745 us,pipeline用时:5882 us,forEach用时:26483 us 
轮次:9,multiGet用时:625 us,pipeline用时:5160 us,forEach用时:25315 us 

key数量:500
轮次:0,multiGet用时:1658 us,pipeline用时:9814 us,forEach用时:69074 us 
轮次:1,multiGet用时:1072 us,pipeline用时:11626 us,forEach用时:65490 us 
轮次:2,multiGet用时:913 us,pipeline用时:9561 us,forEach用时:57722 us 
轮次:3,multiGet用时:1140 us,pipeline用时:10545 us,forEach用时:57030 us 
轮次:4,multiGet用时:1280 us,pipeline用时:9500 us,forEach用时:55476 us 
轮次:5,multiGet用时:926 us,pipeline用时:8460 us,forEach用时:54405 us 
轮次:6,multiGet用时:859 us,pipeline用时:9772 us,forEach用时:54165 us 
轮次:7,multiGet用时:970 us,pipeline用时:7424 us,forEach用时:51297 us 
轮次:8,multiGet用时:1023 us,pipeline用时:7307 us,forEach用时:47868 us 
轮次:9,multiGet用时:866 us,pipeline用时:10094 us,forEach用时:47386 us 

key数量:1000
轮次:0,multiGet用时:1644 us,pipeline用时:11683 us,forEach用时:93793 us 
轮次:1,multiGet用时:1467 us,pipeline用时:13802 us,forEach用时:95734 us 
轮次:2,multiGet用时:1460 us,pipeline用时:27693 us,forEach用时:96507 us 
轮次:3,multiGet用时:1586 us,pipeline用时:16891 us,forEach用时:94651 us 
轮次:4,multiGet用时:1473 us,pipeline用时:13856 us,forEach用时:98131 us 
轮次:5,multiGet用时:1508 us,pipeline用时:36714 us,forEach用时:121501 us 
轮次:6,multiGet用时:1516 us,pipeline用时:18947 us,forEach用时:88077 us 
轮次:7,multiGet用时:1657 us,pipeline用时:14564 us,forEach用时:108498 us 
轮次:8,multiGet用时:1232 us,pipeline用时:13423 us,forEach用时:103207 us 
轮次:9,multiGet用时:1278 us,pipeline用时:13826 us,forEach用时:94357 us 
```

## 远程redis7.0.5 for linux
百度云服务器，redis访问延时较高约30ms，但不影响对比；forEach每一次的速度为30ms，所以直接放弃测试
```
key数量:10
轮次:0,multiGet用时:47784 us,pipeline用时:153081 us 
轮次:1,multiGet用时:33288 us,pipeline用时:130086 us 
轮次:2,multiGet用时:32917 us,pipeline用时:132741 us 
轮次:3,multiGet用时:32624 us,pipeline用时:116800 us 
轮次:4,multiGet用时:31865 us,pipeline用时:128192 us 
轮次:5,multiGet用时:31856 us,pipeline用时:116981 us 
轮次:6,multiGet用时:35771 us,pipeline用时:120602 us 
轮次:7,multiGet用时:31837 us,pipeline用时:138366 us 
轮次:8,multiGet用时:31843 us,pipeline用时:125857 us 
轮次:9,multiGet用时:31747 us,pipeline用时:128107 us 

key数量:20
轮次:0,multiGet用时:32251 us,pipeline用时:120760 us 
轮次:1,multiGet用时:32093 us,pipeline用时:120276 us 
轮次:2,multiGet用时:31999 us,pipeline用时:115696 us 
轮次:3,multiGet用时:31923 us,pipeline用时:127995 us 
轮次:4,multiGet用时:31868 us,pipeline用时:129048 us 
轮次:5,multiGet用时:31652 us,pipeline用时:113914 us 
轮次:6,multiGet用时:32128 us,pipeline用时:122831 us 
轮次:7,multiGet用时:37037 us,pipeline用时:112915 us 
轮次:8,multiGet用时:31666 us,pipeline用时:107655 us 
轮次:9,multiGet用时:31483 us,pipeline用时:133471 us 

key数量:50
轮次:0,multiGet用时:32517 us,pipeline用时:181324 us 
轮次:1,multiGet用时:32015 us,pipeline用时:149790 us 
轮次:2,multiGet用时:31717 us,pipeline用时:124137 us 
轮次:3,multiGet用时:31486 us,pipeline用时:122435 us 
轮次:4,multiGet用时:31626 us,pipeline用时:131635 us 
轮次:5,multiGet用时:31575 us,pipeline用时:103499 us 
轮次:6,multiGet用时:31380 us,pipeline用时:104116 us 
轮次:7,multiGet用时:30430 us,pipeline用时:119714 us 
轮次:8,multiGet用时:31824 us,pipeline用时:129320 us 
轮次:9,multiGet用时:31825 us,pipeline用时:120471 us 

key数量:100
轮次:0,multiGet用时:32322 us,pipeline用时:130215 us 
轮次:1,multiGet用时:31679 us,pipeline用时:150561 us 
轮次:2,multiGet用时:31873 us,pipeline用时:138199 us 
轮次:3,multiGet用时:36754 us,pipeline用时:140471 us 
轮次:4,multiGet用时:32570 us,pipeline用时:145537 us 
轮次:5,multiGet用时:31854 us,pipeline用时:114539 us 
轮次:6,multiGet用时:31083 us,pipeline用时:101197 us 
轮次:7,multiGet用时:31322 us,pipeline用时:122838 us 
轮次:8,multiGet用时:31980 us,pipeline用时:172619 us 
轮次:9,multiGet用时:32675 us,pipeline用时:118260 us 

key数量:200
轮次:0,multiGet用时:32078 us,pipeline用时:142523 us 
轮次:1,multiGet用时:33071 us,pipeline用时:119185 us 
轮次:2,multiGet用时:32467 us,pipeline用时:123588 us 
轮次:3,multiGet用时:32685 us,pipeline用时:152095 us 
轮次:4,multiGet用时:32587 us,pipeline用时:129898 us 
轮次:5,multiGet用时:32665 us,pipeline用时:172389 us 
轮次:6,multiGet用时:32591 us,pipeline用时:99026 us 
轮次:7,multiGet用时:32707 us,pipeline用时:113401 us 
轮次:8,multiGet用时:32277 us,pipeline用时:132569 us 
轮次:9,multiGet用时:32258 us,pipeline用时:128797 us 

key数量:500
轮次:0,multiGet用时:32514 us,pipeline用时:156111 us 
轮次:1,multiGet用时:32804 us,pipeline用时:163141 us 
轮次:2,multiGet用时:32793 us,pipeline用时:164756 us 
轮次:3,multiGet用时:32655 us,pipeline用时:168571 us 
轮次:4,multiGet用时:32595 us,pipeline用时:157115 us 
轮次:5,multiGet用时:32643 us,pipeline用时:186032 us 
轮次:6,multiGet用时:32827 us,pipeline用时:176151 us 
轮次:7,multiGet用时:32210 us,pipeline用时:172908 us 
轮次:8,multiGet用时:32481 us,pipeline用时:156010 us 
轮次:9,multiGet用时:32651 us,pipeline用时:154681 us 

key数量:1000
轮次:0,multiGet用时:33072 us,pipeline用时:198342 us 
轮次:1,multiGet用时:32925 us,pipeline用时:182464 us 
轮次:2,multiGet用时:33614 us,pipeline用时:212318 us 
轮次:3,multiGet用时:33142 us,pipeline用时:182367 us 
轮次:4,multiGet用时:33183 us,pipeline用时:193346 us 
轮次:5,multiGet用时:33371 us,pipeline用时:197015 us 
轮次:6,multiGet用时:32964 us,pipeline用时:184855 us 
轮次:7,multiGet用时:33658 us,pipeline用时:213312 us 
轮次:8,multiGet用时:33264 us,pipeline用时:186512 us 
轮次:9,multiGet用时:32980 us,pipeline用时:207962 us 
```
## 总结
redis版本不影响结果
multiGet速度约为pipeline的5~10倍，key数量越多速度差异越大
