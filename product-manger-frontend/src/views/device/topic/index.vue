<template>
  <div>
    <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :data-source="data"
      :loading="loading"
    >
      <template #access="{ text: access }">
        <a-tag color="green" v-if="access === 1">订阅</a-tag>
        <a-tag color="green" v-else-if="access === 2">发布</a-tag>
        <a-tag color="green" v-else-if="access === 3">订阅/发布</a-tag>
      </template>
      <template #operation="{ record }">
        <a-button size="small" type="primary" style="margin-left: 8px" @click="showMqttDrawer(record)">测试</a-button>
      </template>
    </a-table>

    <a-drawer
        title="设备主题测试"
        placement="right"
        :closable="false"
        width="600px"
        v-model:visible="drawerVisible"
    >
      <mqtt-client ref="mqttClient"
                   :connectionMess="connectionMess"
                   :subscription="subscription"
                   :publish="publish">
      </mqtt-client>
    </a-drawer>
  </div>
</template>
<script>
  import { getDeviceTopicList } from '@/api/topic'
  import MqttClient from '@/components/mqtt/mqtt-client'

  let columns = [
    {
      title: '主题',
      dataIndex: 'topic',
    },
    {
      title: '操作',
      dataIndex: 'access',
      slots: { customRender: 'access' },
    },
    {
      title: 'IP限制',
      dataIndex: 'ipaddr',
    },
    {
      title: '主题描述',
      dataIndex: 'topicDesc',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'operatorAuth',
      slots: { customRender: 'operation' },
    }
  ]

  export default {
    name: 'DeviceTopics',
    components: {
      MqttClient,
    },
    data() {
      return {
        data: [],
        loading: false,
        columns,
        drawerVisible: false,
        connectionMess: {
          host: process.env.VUE_APP_MQTT_SERVER,
          port: 8083,
          endpoint: '/mqtt',
          clean: true, // 保留会话
          connectTimeout: 4000, // 超时时间
          reconnectPeriod: 4000, // 重连时间间隔
          // 认证信息
          clientId: 'mqttjs_3be2c321',
          username: 'emqx_1',
          password: 'nth0eZT9',
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
    mounted() {
      this.connectionMess.clientId = this.$route.params.clientId
      this.connectionMess.username = this.$route.params.username
      this.connectionMess.password = this.$route.params.password
      this.fetch()
    },
    methods: {
      showMqttDrawer(record) {
        this.drawerVisible = true
        if (record.access === 1) {
          this.subscription.topic = record.topic
        } else if (record.access === 2) {
          this.publish.topic = record.topic
        } else if (record.access === 3) {
          this.subscription.topic = record.topic
          this.publish.topic = record.topic
        }
      },
      fetch(filters) {
        this.loading = true
        getDeviceTopicList(this.$route.params.id).then((res) => {
          this.data = res.data
          this.loading = false
        })
      },
    },
  }
</script>
