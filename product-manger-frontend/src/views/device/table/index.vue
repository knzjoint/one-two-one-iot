<template>
  <div>
    <a-button type="primary" @click="showModal">创建设备</a-button>
    <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :data-source="data"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template #connectStatus="{ text: connectStatus }">
        <a-tag color="green" v-if="connectStatus">在线</a-tag>
        <a-tag color="red" v-else>断开</a-tag>
      </template>
      <template #operation="{ record }">
        <a-popconfirm
          title="是否复制设备账户密码?"
          @confirm="onCopyDeviceMessage(e, record)"
        >
          <a style="margin-left: 8px">复制</a>
        </a-popconfirm>
        <a-button size="small" type="primary" style="margin-left: 8px" @click="showDeviceTopic(record)">主题</a-button>
        <a-button size="small" type="primary" style="margin-left: 8px" @click="showThingsModel(record)">物模型</a-button>
        <a-button size="small" type="primary" style="margin-left: 8px" @click="showEditTopic(record)">编辑</a-button>
        <a-popconfirm
            title="是否删除此主题?"
            @confirm="_deleteDeviceOne(record)"
        >
          <a-button size="small" style="margin-left: 8px" type="primary">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>

    <a-modal
      title="创建设备"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="addApplicationToken"
      @cancel="handleCancel"
    >
      <a-form
        :model="deviceForm"
        :label-col="{ span: 4 }"
        :rules="deviceFormRules"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="设备名" name="username">
          <a-input v-model:value="deviceForm.username" placeholder="设备名" />
        </a-form-item>
        <!--<a-form-item label="设备ID">-->
          <!--<a-input v-model:value="deviceForm.clientId" placeholder="设备ID" />-->
        <!--</a-form-item>-->
        <a-form-item label="密码">
          <a-input v-model:value="deviceForm.password" placeholder="密码" />
        </a-form-item>
        <a-form-item label=" 产 品 ">
          <a-select style="width: 120px" v-model:value="deviceForm.productId">
            <a-select-option
              v-for="item in productData"
              :value="item.id"
              :key="item.productEnName"
            >
              {{ item.productName }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
  import { getPage, addDevice, deleteDevice } from '@/api/device'
  import { getProductList } from '@/api/product'
  import { copyHandle } from '@/utils/clipboard'

  const columns = [
    {
      title: '设备账号',
      dataIndex: 'username',
    },
    {
      title: '密码',
      dataIndex: 'password',
    },
    {
      title: '设备ID',
      dataIndex: 'clientId',
    },
    {
      title: '所属产品',
      dataIndex: 'productName',
    },
    {
      title: '连接状态',
      dataIndex: 'connectStatus',
      slots: { customRender: 'connectStatus' },
      filters: [
        { text: '在线', value: true },
        { text: '离线', value: false },
      ],
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'operation',
      slots: { customRender: 'operation' },
    },
  ]

  export default {
    data() {
      return {
        data: [],
        pagination: {
          showLessItems: true,
          showQuickJumper: true,
          showSizeChanger: true,
        },
        query: {},
        loading: false,
        columns,
        deviceForm: {
          username: undefined,
          productId: 1,
          password: undefined,
        },
        deviceFormRules: {
          username: [
            { required: true, message: '请输入设备名称!', trigger: 'blur' },
            { required: true, validator: this.validateDeviceName, trigger: 'change' }
          ]
        },
        visible: false,
        confirmLoading: false,
        productData: [],
      }
    },
    mounted() {
      let filters = {
        connectStatus: [],
      }
      this.getProductList()
      this.fetch(filters)
    },
    methods: {
      async validateDeviceName(_rule, value) {
        // 用户名正则，8到16位（字母，数字，下划线，减号, $）
        const reg = /^[a-zA-Z][a-zA-Z0-9_]{6,15}$/
        // 输出 true
        if (!reg.test(value)) {
          return Promise.reject('以字母开头，6到16位（字母，数字，下划线，减号, $）')
        }
        return Promise.resolve()
      },
      showModal() {
        this.visible = true
        this.getProductList()
      },
      showDeviceTopic(record) {
        this.$router.push({ name: 'DeviceTopics',
          params: {
            id: record.id,
            clientId: record.clientId,
            username: record.username,
            password: record.password,
          }
        })
      },
      showThingsModel(record) {
        this.$router.push({ name: 'ThingsTable',
          query: {
            clientId: record.clientId,
          },
          params: {
            clientId: record.clientId,
          }
        })
      },
      addApplicationToken() {
        this.confirmLoading = true
        addDevice(this.deviceForm)
          .then(() => {
            this.visible = false
            this.fetch()
          })
          .finally(() => {
            this.confirmLoading = false
          })
      },
      onCopyDeviceMessage(event, record) {
        let data =
          'clientId=' +
          record.clientId +
          ', deviceName=' +
          record.username +
          ', password=' +
          record.password

        copyHandle(data)
      },
      handleCancel() {
        this.visible = false
      },
      handleTableChange(pagination, filters) {
        const pager = { ...this.pagination }
        pager.current = pagination.current
        this.pagination = pager

        this.fetch(filters)
      },
      getProductList() {
        getProductList().then((res) => {
          this.productData = res.data

          // 更新通过产品筛选
          let filters = this.productData.map((e) => {
            return { text: e.productName, value: e.id }
          })
          this.columns.forEach((e) => {
            if (e.dataIndex === 'productName') {
              e.filters = filters
            }
          })
        })
      },
      _deleteDeviceOne(record) {
        deleteDevice(record.id).then((res) => {
          this.fetch()
          this.$message.success(res.msg)
        }).catch((err) => {
          this.$message.warning(err.msg)
        })
      },
      fetch(filters) {
        this.loading = true
        let requestData = {
          pageSize: this.pagination.pageSize,
          current: this.pagination.current,
          ...filters,
        }
        getPage({
          ...requestData,
        }).then((res) => {
          this.loading = false
          // const pagination = { ...this.pagination }
          this.pagination.total = res.data.total
          // this.loading = false
          this.data = res.data.records
          // this.pagination = pagination
        })
      },
    },
  }
</script>
