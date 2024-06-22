# 基础镜像
FROM openjdk:8-jre
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝jar包
COPY XiaoMiCar.jar /XiaoMiCar.jar
# 入口
ENTRYPOINT ["java", "-jar", "/XiaoMiCar.jar"]