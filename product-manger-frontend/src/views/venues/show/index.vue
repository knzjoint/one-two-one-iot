<template>
  <div>
    <a-button type="primary" @click="showModal">添加场地/房间</a-button>

    <splitpanes class="default-theme">
      <pane size="18" max-size="24">
        <a-list :grid="{ gutter: 24, column: 1 }" :data-source="data" style="width: 100%">
          <template #renderItem="{ item }">
            <a-list-item>
              <a-card :title="item.venuesName" @contextmenu="showContainerContextMenu($event, item)">
                <p>{{ item.venuesDesc }}</p>
                <div @click="handleVenuesClick(item.id)">
                  <img :src="item.venuesIcon" style="width: 100%" alt="">
                </div>
                <a-button @click="clickWorkFlow(item)">场景联动</a-button>
              </a-card>
            </a-list-item>
          </template>
        </a-list>
      </pane>
      <pane>
        <div class="things-container">
          <div class="things-card" v-for="(item, index) in venuesThings" :key="index">
            <a-card :title="item.name + '#' + item.sequence">
              <p>{{ item.name }} : {{ item.thingsValue }}</p>
              <div>
                <img :src="item.thingsIcon" style="width: 120px; height: 120px;" alt="">
              </div>
              <div>
                <things-control :things-id="item.id" :value="item.thingsValue" :control-ui="JSON.parse(item.controlUi)"
                                :data-format="JSON.parse(item.dataFormat)"></things-control>
              </div>
            </a-card>
          </div>
        </div>
      </pane>
    </splitpanes>

    <a-drawer
        title="物控制"
        placement="right"
        :closable="false"
        width="720"
        :body-style="{ paddingBottom: '80px' }"
        v-model:visible="drawerVisible"
    >
      <a-form
          :model="venuesForm"
          :label-col="{ span: 20 }"
          :rules="venuesFormRules"
          layout="vertical"
          :wrapper-col="{ span: 20 }"
      >

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="name" label="名称">
              <a-input v-model:value="venuesForm.venuesName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="图标">
              <single-image-upload v-model:src="venuesForm.venuesIcon"></single-image-upload>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="物描述">
              <a-textarea v-model:value="venuesForm.thingsDesc"></a-textarea>
            </a-form-item>
          </a-col>

          <a-col :span="20">
            <a-form-item name="deviceIds" label="选择设备">
              <a-select
                  v-model:value="venuesForm.deviceIds"
                  mode="multiple"
                  style="width: 100%"
                  placeholder="选择设备"
                  option-label-prop="label"
              >
                <a-select-option v-for="(item, index) in deviceList" :value="item.id" :label="item.username"
                                 :key="index">
                  {{ item.username }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!--选择数据模型-->

        </a-row>

        <a-row style="margin-top: 20px">
          <a-col :span="18"></a-col>
          <a-col :span="6">
            <a-button @click="_addVenues" type="primary">确认</a-button>
          </a-col>
        </a-row>

      </a-form>
    </a-drawer>

    <context-menu name="venues-menu">
      <context-menu-item @itemClickHandle="copyThingsTopic" :divider="true">复制主题</context-menu-item>
      <context-menu-item @itemClickHandle="_deleteVenues" :divider="true">删除</context-menu-item>
    </context-menu>

  </div>
</template>
<script>
  import {addVenues, getVenuesList, getVenuesThings, deleteVenues} from '@/api/venues'
  import {getDataTypeList} from '@/api/dataType'
  import {getDeviceList} from '@/api/device'
  import {validateDeviceName} from '@/utils/validate'
  import SingleImageUpload from '@/components/single-image-upload'
  import ThingsControl from '../../device/components/things-control'

  import { emitContext, hideContext } from 'vue3-contextmenu'
  import {Pane, Splitpanes} from 'splitpanes'
  import 'splitpanes/dist/splitpanes.css'

  export default {
    name: 'Venues',
    components: {
      SingleImageUpload,
      ThingsControl,
      Pane,
      Splitpanes,
    },
    data() {
      return {
        data: [],
        deviceList: [],
        venuesThings: [],
        formatTypeValue: 0, // 数据格式类型
        venuesForm: {
          venuesName: '',
          venuesIcon: '',
          venuesDesc: '',
          deviceIds: [],
        },
        venuesFormRules: {
          venuesName: [
            {required: true, message: '请输入标识!', trigger: 'blur'},
          ],
        },
        loading: false,
        drawerVisible: false,
        confirmLoading: false,
      }
    },
    mounted() {
      this.fetch()
      this._getDeviceList()

      this.$bus.$on('THINGS_CHANGED', key => {
        this.venuesThings = this.venuesThings.map(item => item.id === key.id ? key : item)
      })
    },
    methods: {
      showContainerContextMenu(e, item) {
        emitContext(e, { name: 'venues-menu', id: item.id, })
      },
      clickWorkFlow(item) {

        this.$router.push({ name: 'ThingsLinkage',
          query: {
            venuesId: item.id,
          },
          params: {
            venuesId: item.id,
          }
        })
      },
      controlsSelectChange() {

      },
      handleVenuesClick(id) {
        this._getVenuesThings(id)
      },
      formatTypeSelectChange(value) {
        this.thingsForm.controls = undefined
        this.formatTypeValue = value
      },
      showModal() {
        this.drawerVisible = true
      },
      fetch(filters) {
        this.loading = true
        getVenuesList().then((res) => {
          this.data = res.data
          if (this.data.length >= 1) {
            this._getVenuesThings(this.data[0].id)
          }
          this.loading = false
        })
      },
      _addVenues() {
        addVenues(this.venuesForm).then(res => {
          this.fetch()
          this.$message.success('添加成功！')
          this.drawerVisible = false
        }).catch(() => {
          this.$message.error('添加失败！')
        })
      },
      _getDeviceList() {
        getDeviceList().then(res => {
          this.deviceList = res.data
        })
      },
      _getVenuesThings(id) {
        getVenuesThings(id).then(res => {
          this.venuesThings = res.data
        })
      },
      _deleteVenues(item) {
        deleteVenues(item.id).then(res => {
          this.$message.success('删除成功！')
          this.fetch()
        }).catch(() => {
          this.$message.error('删除失败！')
        })
      }
    },
  }
</script>

<style>

  .things-container {
    display: flex;
    flex-wrap: wrap;
    /*justify-content: space-around*/
  }

  .things-card {
    display: inline-block;
    align-content: flex-start;
    width: 180px;
    height: 360px;
    margin: 10px;
  }

</style>
