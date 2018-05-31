FROM centos
# Update CentOS 7
RUN yum update -y && yum upgrade -y

# Install packages
RUN yum install -y unzip wget curl git

RUN yum install -y java-1.8.0-openjdk-devel

RUN yum clean all

VOLUME /tmp
ADD maven/${project.artifactId}-${project.version}.jar recordservice.jar
RUN sh -c 'touch /recordservice.jar'

ADD maven/application.yml application.yml
RUN sh -c 'touch /application.yml'

ADD maven/src/main/resources/data/Workbook.csv src/main/resources/data/Workbook.csv
RUN sh -c 'touch src/main/resources/data/Workbook.csv'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/recordservice.jar", "--spring.config.location=/application.yml","-Dspring.profiles.active=default,consul"]
