<template>
  <div>
    <a-button type="primary" @click="showModal">添加物</a-button>

    <div class="things-container">
      <div class="things-card" v-for="(item, index) in data" :key="index">
        <a-card :title="item.name + '#' + item.sequence"
                @contextmenu="showContainerContextMenu($event, item)"
                @dblclick="copyThingsTopic(item)"
        >
          <p style="width:150px;white-space: nowrap; text-overflow:ellipsis; overflow:hidden;">
            {{ item.name }} : {{ item.thingsValue }}
          </p>
          <div>
            <img :src="item.thingsIcon" style="width: 100%" alt="">
          </div>
          <div>
            <things-control :things-id="item.id" :value="item.thingsValue" :control-ui="JSON.parse(item.controlUi)"
                            :data-format="JSON.parse(item.dataFormat)"></things-control>
          </div>
        </a-card>
      </div>
    </div>

    <a-drawer
        title="物控制"
        placement="right"
        :closable="false"
        width="720"
        :body-style="{ paddingBottom: '80px' }"
        v-model:visible="drawerVisible"
    >

      <a-form
          :model="thingsForm"
          :label-col="{ span: 20 }"
          :rules="thingsFormRules"
          layout="vertical"
          :wrapper-col="{ span: 20 }"
      >

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="name" label="名称">
              <a-input v-model:value="thingsForm.name"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12">-->
          <!--<a-form-item label="图标">-->
          <!--<a-input v-model:value="thingsForm.thingsIcon"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->

          <a-col :span="12">
            <a-form-item label="图标">
              <single-image-upload v-model:src="thingsForm.thingsIcon"></single-image-upload>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="thingsName" label="标识">
              <a-input v-model:value="thingsForm.thingsName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="序列号">
              <a-input v-model:value="thingsForm.serialNumber"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="版本">
              <a-input v-model:value="thingsForm.version"></a-input>
            </a-form-item>
          </a-col>


          <a-col :span="12">
            <a-form-item label="物描述">
              <a-textarea v-model:value="thingsForm.thingsDesc"></a-textarea>
            </a-form-item>
          </a-col>


          <!--选择数据模型-->


          <a-col :span="8">
            <a-form-item name="isShow" label="是否显示">
              <a-switch checked-children="显示" un-checked-children="不显示" v-model:checked="thingsForm.isShow"/>
            </a-form-item>
          </a-col>


          <a-col :span="8">
            <a-form-item name="rwType" label="只读">
              <a-radio-group v-model:value="thingsForm.rwType">
                <a-radio-button value="1">读</a-radio-button>
                <a-radio-button value="2">写</a-radio-button>
                <a-radio-button value="3">读写</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>


          <a-col :span="8">
            <a-form-item name="isStartup" label="是否启动">
              <a-switch checked-children="启动" un-checked-children="关闭" v-model:checked="thingsForm.isStartup"/>
            </a-form-item>
          </a-col>

          <a-divider :plain="true">数据模型</a-divider>

          <a-col :span="12">
            <a-form-item label="数据类型">
              <a-select v-model:value="thingsForm.dataTypeId" @change="formatTypeSelectChange">
                <a-select-option v-for="(item, index) in dataTypeList" :value="item.id" :key="index">{{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>


          <a-col :span="12">
            <a-form-item label="控制UI">
              <a-select v-model:value="thingsForm.controlType" @change="controlsSelectChange">
                <a-select-option v-for="(item, index) in controlUiSelect" :value="item.value" :key="index"
                                 :disabled="item.disabled(formatTypeValue)">{{ item.label }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>


          <template v-if="formatTypeValue === 1">
            <int-data-format v-model:dataFormat="thingsForm.dataFormat"></int-data-format>
          </template>

          <template v-if="formatTypeValue === 2">
            <decimal-data-format v-model:dataFormat="thingsForm.dataFormat"></decimal-data-format>
          </template>

          <template v-if="formatTypeValue === 3">
            <string-data-format v-model:dataFormat="thingsForm.dataFormat"></string-data-format>
          </template>

          <template v-if="formatTypeValue === 4">
            <bool-data-format v-model:dataFormat="thingsForm.dataFormat"></bool-data-format>
          </template>

          <template v-if="formatTypeValue === 5">
            <json-data-format v-model:dataFormat="thingsForm.dataFormat"></json-data-format>
          </template>

        </a-row>

        <a-row>
          <a-button @click="_addThings">确认</a-button>
        </a-row>

      </a-form>
    </a-drawer>

    <context-menu name="context-node">

      <context-menu-item @itemClickHandle="copyThingsTopic" :divider="true">复制主题</context-menu-item>
      <context-menu-item @itemClickHandle="deleteThingsTopic" :divider="true">删除主题</context-menu-item>

    </context-menu>

  </div>
</template>
<script>
  import {addThings, getClientThings, deleteThings, copyThingsTopic} from '@/api/things'
  import {getDataTypeList} from '@/api/dataType'
  import {validateDeviceName} from '@/utils/validate'
  import BoolDataFormat from '@/components/data-type/bool-data-format'
  import DecimalDataFormat from '@/components/data-type/decimal-data-format'
  import IntDataFormat from '@/components/data-type/int-data-format'
  import StringDataFormat from '@/components/data-type/string-data-format'
  import JsonDataFormat from '@/components/data-type/json-data-format'
  import ThingsControl from '../components/things-control'
  import SingleImageUpload from '@/components/single-image-upload'

  import { emitContext, hideContext } from 'vue3-contextmenu'
  import { copyHandle } from '@/utils/clipboard'

  const controlUiSelect = [
    {
      value: 'BUTTON',
      label: 'BUTTON',
      disabled: (type) => {
        return ![4].includes(type)
      },
    },
    {
      value: 'INPUT',
      label: 'INPUT',
      disabled: (type) => {
        return ![1, 2].includes(type)
      },
      // disabled: true,
    },
    {
      value: 'SLIDER',
      label: 'SLIDER',
      disabled: (type) => {
        return ![1, 2].includes(type)
      },
    },
    {
      value: 'SWITCH',
      label: 'SWITCH',
      disabled: (type) => {
        return ![4].includes(type)
      },
    },
    {
      value: 'TEXT_INPUT',
      label: 'TEXT_INPUT',
      disabled: (type) => {
        return ![3].includes(type)
      },
    },
    {
      value: 'JSON_INPUT',
      label: 'JSON_INPUT',
      disabled: (type) => {
        return ![5].includes(type)
      },
    },
  ]

  export default {
    name: 'Things',
    components: {
      BoolDataFormat,
      DecimalDataFormat,
      IntDataFormat,
      StringDataFormat,
      ThingsControl,
      SingleImageUpload,
      JsonDataFormat,
    },
    data() {
      return {
        clientId: '',
        data: [],
        dataTypeList: [],
        formatTypeValue: 0, // 数据格式类型
        thingsForm: {
          name: '',
          thingsIcon: '',
          thingsName: '', // 限制为英文
          serialNumber: '', // 产品的序列号
          version: '', // 产品版本
          thingsDesc: '', // 物描述
          isShow: true, // 在列表中显示
          rwType: '1', // 1 是只读, 2 只写， 3 读写
          isStartup: true, // 是否启动
          dataTypeId: undefined, // 数据类型
          controlType: undefined, // UI类型
          dataFormat: {
            trueText: '测试',
            falseText: '',
          }, // 数据格式
        },
        thingsFormRules: {
          thingsName: [
            {required: true, message: '请输入标识!', trigger: 'blur'},
            {required: true, validator: validateDeviceName, trigger: 'change'}
          ],
          isShow: [
            {required: true, message: '请选择是否显示!', trigger: 'blur'},
          ],
          rwType: [
            {required: true, message: '请选择是否显示!', trigger: 'blur'},
          ],
          isStartup: [
            {required: true, message: '请选择是否显示!', trigger: 'blur'},
          ]
        },
        controlUiSelect,
        loading: false,
        drawerVisible: false,
        confirmLoading: false,
      }
    },
    mounted() {
      this.thingsForm.clientId = this.$route.query.clientId
      this.clientId = this.$route.query.clientId

      this.fetch()
      this._getDataTypeList()

      this.$bus.$on('THINGS_CHANGED', key => {
        this.data = this.data.map(item => item.id === key.id ? key : item)
      })
    },
    methods: {
      showContainerContextMenu(e, item) {
        emitContext(e, { name: 'context-node', id: item.id, })
      },
      copyThingsTopic(context) {
        copyThingsTopic(context.id).then(res => {
          copyHandle(res.data)
        }).catch(e => {
          this.$message.error('复制错误!')
        })
      },
      deleteThingsTopic(context) {
        deleteThings(context.id).then(res => {
          this.$message.success('删除成功!')
          this.fetch()
        }).catch(e => {
          this.$message.error('删除物模型错误!')
        })
      },
      controlsSelectChange() {

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
        getClientThings(this.clientId).then((res) => {
          this.data = res.data
          this.loading = false
        })
      },
      _addThings() {
        addThings(this.thingsForm).then(res => {
          this.drawerVisible = false
          this.$message.success('添加成功')
          this.fetch()
        }).catch((err) => {
          this.$message.warning(err.msg)
        })
      },
      _getDataTypeList() {
        getDataTypeList().then(res => {
          this.dataTypeList = res.data
        })
      },
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
