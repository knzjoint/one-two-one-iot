<template>
  <div class="mqtt-container">
    <a-card title="配置" shadow="always" style="margin-bottom:30px;">
      <a-form ref="configForm" :form="connectionMess">
        <a-row :gutter="20">
          <a-col :span="8">
            <a-form-item name="host" label="主机">
              <a-input v-model:value="connectionMess.host"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="端口">
              <a-input v-model:value="connectionMess.port" type="number"
                       placeholder="8083/8084"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="Path">
              <a-input v-model:value="connectionMess.endpoint" placeholder="/mqtt"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="客户端ID">
              <a-input v-model:value="connectionMess.clientId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="用户名">
              <a-input v-model:value="connectionMess.username"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="密码">
              <a-input v-model:value="connectionMess.password"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-button
                type="primary"
                size="small"
                class="conn-btn"
                style="margin-right: 20px;"
                :loading="connectedLoading"
                :disabled="connected"
                @click="createConnection"
            >
              {{ connected ? '断开' : '连接' }}
            </a-button>

            <a-button v-if="connected" type="danger" size="small" class="conn-btn"
                      @click="destroyConnection">
              断开连接
            </a-button>
          </a-col>
        </a-row>
      </a-form>
    </a-card>
    <a-card title="订阅" shadow="always" style="margin-bottom:30px;">
      <a-form ref="subscription" hide-required-asterisk size="small" label-position="top" :form="subscription">
        <a-row :gutter="20">
          <a-col :span="16">
            <a-form-item label="主题">
              <a-input v-model:value="subscription.topic"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="20">
          <a-col :span="8">
            <a-form-item label="QoS">
              <a-select :value="subscription.qos" placeholder="请选择qos">
                <a-select-option
                    v-for="(item, index) in qosList"
                    :key="item.vlue"
                    :value="item.value"
                >{{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="5"></a-col>
          <a-col :span="8">
            <a-button
                :disabled="!connected"
                type="primary"
                size="small"
                class="subscribe-btn"
                @click="doSubscribe"
            >
              {{ '订阅' }}
            </a-button>
          </a-col>
        </a-row>
      </a-form>
      <a-row>
        <a-col :span="20">
          <a-list item-layout="horizontal" :data-source="subscripTopics">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta
                    :description="item.topic"
                >
                  <template #title>
                    <a href="https://www.antdv.com/">{{ item.title }}</a>
                  </template>
                  <template #avatar>
                    <a-avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"/>
                  </template>
                </a-list-item-meta>
                <a-button @click="doUnSubscribe(item)" size="small">取消</a-button>
              </a-list-item>
            </template>
          </a-list>
        </a-col>
      </a-row>
    </a-card>
    <a-card title="发布" shadow="always" style="margin-bottom:30px;">
      <a-form ref="publish" hide-required-asterisk size="small" label-position="top" :model="publish">
        <a-row :gutter="24">
          <a-col :span="16">
            <a-form-item label="主题">
              <a-input v-model:value="publish.topic"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="QoS">
              <a-select v-model:value="publish.qos">
                <a-select-option
                    v-for="(item, index) in qosList"
                    :key="index"
                    :value="item.value"
                >{{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="16">
            <a-form-item label="消息">
              <a-textarea v-model:value="publish.payload" placeholder="Basic usage" :rows="4"/>
              <!--<a-input v-model:value="publish.payload" size="small"></a-input>-->
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <a-col :span="24">
        <a-button :disabled="!connected" type="success" size="small" class="publish-btn" @click="doPublish">
          发布
        </a-button>
      </a-col>
    </a-card>
    <a-card title="接收消息" shadow="always" style="margin-bottom:30px;">
      <a-col :span="24">
        <a-list item-layout="horizontal" :data-source="receiveMsg">
          <a-list-item slot="renderItem" slot-scope="item, index">
            {{ item }}
          </a-list-item>
        </a-list>

        <a-list item-layout="horizontal" :data-source="receiveMsg">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta v-if="item.type === 'publish'" :description="item.topic">
                <template #title>
                  <div>{{ item.payload }}</div>
                </template>
                <template #avatar>
                  <a-avatar src="http://image.makerknz.cn/image/P_round_solid_p_by_climei.png"/>
                </template>
              </a-list-item-meta>
              <a-list-item-meta v-if="item.type === 'subscribe'" :description="item.topic">
                <template #avatar>
                  <a-avatar src="http://image.makerknz.cn:9000/image/S_round_solid_s_by_climei.png"/>
                </template>
                <template #title>
                  <div>{{ item.payload }}</div>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>

      </a-col>
    </a-card>
  </div>
</template>

<script>
  import mqtt from 'mqtt'

  export default {
    name: 'MqttClient',
    props: {
      connectionMess: {
        type: Object,
        default() {
          return {
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
          }
        },
      },
      subscription: {
        type: Object,
        default() {
          return {
            topic: 'topic/mqttx',
            qos: 0,
          }
        },
      },
      publish: {
        type: Object,
        default() {
          return {
            topic: 'topic/browser',
            qos: 0,
            payload: '{ "msg": "Hello, I am browser." }',
          }
        }
      }
    },
    data() {
      return {
        receiveMsg: [],
        qosList: [
          {label: 0, value: 0},
          {label: 1, value: 1},
          {label: 2, value: 2},
        ],
        client: {
          connected: false,
        },
        subscripTopics: [],
        connected: false,
        connectedLoading: false,
        subscribeSuccess: false,
      }
    },
    methods: {
      // 创建连接
      createConnection() {
        const {host, port, endpoint, ...options} = this.connectionMess
        const connectUrl = `ws://${host}:${port}${endpoint}`
        // options.clientId = this.connectionMess.username
        this.connectedLoading = true
        try {
          this.client = mqtt.connect(connectUrl, options)
        } catch (error) {
          this.connectedLoading = false
          console.log('mqtt.connect error', error)
        }
        this.client.on('connect', () => {
          this.client.connected = true
          this.connected = true
          this.connectedLoading = false
          console.log('Connection succeeded!')
        })
        this.client.on('error', error => {
          this.$message.error(error.toString())
          if ('Error: Connection refused: Not authorized' === error.toString()) {
            this.client.end()
            this.connectedLoading = false
          }
          console.log('Connection failed', error)
        })
        this.client.on('message', (topic, message) => {
          this.connectedLoading = false
          let msg = {}
          msg.type = 'subscribe'
          msg.topic = topic
          msg.qos = 0
          msg.payload = message
          this.receiveMsg.push(msg)
          console.log(`Received message ${message} from topic ${topic}`)
        })
        // this.client.on('packetsend', (packet) => {
        //     console.log(packet)
        //     console.log(`Received message ${packet} from topic`)
        // })
        // 解决websocket错误
        this.client.stream.on('error', (err) => {
            if (this.connectedLoading) {
              this.$message.error('连接错误，检查主机')
              this.connectedLoading = false
            }
            console.log('error', err)
            this.client.end()
          }
        )
      },
      // 订阅主题
      doSubscribe() {
        const {topic, qos} = this.subscription
        console.log(this.subscription)
        this.client.subscribe(topic, {qos}, (error, granted) => {
          console.log('Subscribe to topics error', error)
          if (error) {
            this.$message.error('订阅错误:' + error.toString())
            console.log('Subscribe to topics error', error)
            return
          }

          if (granted.length > 0) {
            if (granted[0].qos === 128) {
              this.$message.error('订阅权限错误')
              return
            }
          }

          this.subscribeSuccess = true
          const subTopic = {
            topic,
            qos
          }

          let subed = false
          this.subscripTopics.forEach((subItem, index) => {
            if (subItem.topic === topic) {
              // this.subscripTopics.splice(index, 1)
              this.$message.warning('主题已经订阅')
              subed = true
            }
          })

          if (!subed) {
            this.subscripTopics.push(subTopic)
            this.$message.success('订阅成功:' + granted)
            console.log('Subscribe to topics res', granted)
          }
        })
      },
      // 取消订阅
      doUnSubscribe(item) {
        // const {topic} = this.subscription
        this.client.unsubscribe(item.topic, error => {
          if (error) {
            this.$message.error('取消订阅错误:' + error.toString())
            console.log('Unsubscribe error', error)
            return
          }

          this.subscripTopics.forEach((subItem, index) => {
            if (subItem.topic === item.topic) {
              this.subscripTopics.splice(index, 1)
            }
          })
          this.$message.success('取消订阅成功')
        })
      },
      // 发送消息
      doPublish() {
        const {topic, qos, payload} = this.publish
        this.client.publish(topic, payload, {qos, retain: false}, error => {
          if (error) {
            console.log('Publish error', error)
          } else {
            console.log('Published')
            this.publish.type = 'publish'
            this.receiveMsg.push(this.publish)
          }
        })
      },
      // 断开连接
      destroyConnection() {
        if (this.client.connected) {
          try {
            this.client.end()
            this.connected = false
            console.log('Successfully disconnected!')
          } catch (error) {
            console.log('Disconnect failed', error.toString())
          }
        }
      },
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
