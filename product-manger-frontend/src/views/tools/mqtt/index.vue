<template>
  <div>
    <mqtt-client ref="mqttClient"
                 :connectionMess="connectionMess"
                 :subscription="subscription"
                 :publish="publish"
    ></mqtt-client>
  </div>
</template>

<script>
  import MqttClient from '@/components/mqtt/mqtt-client'

  export default {
    name: 'Home',
    data() {
      return {
        connectionMess: {
          host: process.env.VUE_APP_MQTT_SERVER,
          port: 8083,
          endpoint: '/mqtt',
          clean: true, // 保留会话
          connectTimeout: 4000, // 超时时间
          reconnectPeriod: 4000, // 重连时间间隔
          // 认证信息
          clientId: 'e443ec31b8db42b6882c0d0cf1600e7a',
          username: 'emqx',
          password: '123456',
        },
        subscription: {
          topic: 'topic/mqttx',
          qos: 0,
        },
        publish: {
          topic: 'topic/browser',
          qos: 0,
          payload: '{ "msg": "Hello, I am browser." }',
        }
      }
    },
    components: {
      MqttClient,
    },
  }
</script>

<style>
  /*.mqtt-container {*/
  /*max-width: 1100px;*/
  /*margin: 0 auto;*/

  .mqtt-container {
    background: #ECECEC;
    padding: 30px;
  }

  .conn-btn {
    color: #fff;
    background-color: #4DA9FF;
    font-size: 14px;
  }
</style>
