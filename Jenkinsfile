pipeline {
   agent any

   options {
    	//超过5分钟没有构建完成会退出构建
        timeout(time: 5, unit: 'MINUTES')
        //保持构建的最大个数
        buildDiscarder(logRotator(numToKeepStr: '20'))
   }

   tools {
        //需要在jenkins配置maven环境，名称为maven3.6.1
        maven 'maven3.6.1'
   }
   //常量参数
   environment{
	    //项目
       pipeline_name='jingnan_mall_demo'
       jenkins_workspace_home='data/jenkins_data_temp/workspace'
       artifactId='jingnan_web'
       project_jar_name='${artifactId}-${VERSION}.jar'
       project_home='${pipeline_name}/${artifactId}'
       h_port='22'
       h_user='root'
   }
   stages {
       stage('拉取代码') {
            when {
                expression {
                	return  ("${DEPLOY}" == "upgrade")
                }
            }
            steps {
                git credentialsId: 'git_account', url: '${URL}', branch:"${BRANCH}"

            }
      }
      stage('单元测试') {
            when {
                expression {
                	return  ("${DEPLOY}" == "upgrade")
                }
            }
            steps {
    	       //执行单元测试
                sh "mvn clean test -pl ${artifactId} -am"
    	        //单元测试报告位置
                junit '**/target/surefire-reports/*.xml'
            }
      }
      stage('项目打包') {
            when {
                expression {
                	return  ("${DEPLOY}" == "upgrade")
                }
            }
            steps {
                //执行打包
                sh "mvn clean package -Dmaven.test.skip=true  -pl ${artifactId} -am"
         }
      }

      stage('部署|回滚') {
            steps {
                script {
                    //使用版本号
                    //清空发布目标服务器文件夹
                sh "sshpass -p ${h_pwd} ssh -p ${h_port} ${h_user}@${h_ip} 'rm -f /usr/local/src/${artifactId}/latest/*.*'"
                sh "sshpass -p ${h_pwd} scp -P ${h_port} /usr/local/src/${artifactId}/${VERSION}/${artifactId}-${VERSION}.jar ${h_user}@${h_ip}:/usr/local/src/${artifactId}/latest"
                sh "sshpass -p ${h_pwd} scp -P ${h_port} /usr/local/src/${artifactId}/${VERSION}/startup.sh ${h_user}@${h_ip}:/usr/local/src/${artifactId}/latest"
                sh "sshpass -p ${h_pwd} scp -P ${h_port} /usr/local/src/${artifactId}/${VERSION}/stop.sh ${h_user}@${h_ip}:/usr/local/src/${artifactId}/latest"
                //停止服务
                sh "sshpass -p ${h_pwd} ssh -p ${h_port} ${h_user}@${h_ip} 'chmod 755 /usr/local/src/${artifactId}/latest/stop.sh'"
                sh "sshpass -p ${h_pwd} ssh -p ${h_port} ${h_user}@${h_ip} '/usr/local/src/${artifactId}/latest/stop.sh'".
                 //启动服务
                sh "sshpass -p ${h_pwd} ssh -p ${h_port} ${h_user}@${h_ip} 'chmod 755 /usr/local/src/${artifactId}/latest/startup.sh'"
                sh "sshpass -p ${h_pwd} ssh -p ${h_port} ${h_user}@${h_ip} '/usr/local/src/${artifactId}/latest/startup.sh'"
                }

         }
      }
   }
}
