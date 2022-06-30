<template>
  <div>
    <a-button type="primary" @click="showModal">创建应用</a-button>
    <a-table
        :columns="columns"
        :row-key="(record) => record.uuid"
        :data-source="data"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
    >
      <template #operation="{ record }">
        <a-popconfirm
            title="是否复制设备账户密码?"
            @confirm="onCopyApplicationToken(e, record)"
        >
          <a style="margin-right: 8px;">复制</a>
        </a-popconfirm>
        <a-popconfirm
            title="是否删除此主题?"
            @confirm="_deleteApplicationOne(record)"
        >
          <a-button size="small" type="primary">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
    <a-modal
        title="创建应用"
        :visible="visible"
        :confirm-loading="confirmLoading"
        @ok="addApplicationToken"
        @cancel="handleCancel"
    >
      <a-form
          :model="applicationTokenForm"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="应用名">
          <a-input
              v-model:value="applicationTokenForm.applicationName"
              placeholder="应用名"
          />
        </a-form-item>
        <a-form-item label=" 产 品 ">
          <a-select
              style="width: 120px"
              v-model:value="applicationTokenForm.productId"
          >
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
  import { addApplication, getPage, deleteApplicationToken } from '@/api/application'
  import { getProductList } from '@/api/product'
  import { copyHandle } from '@/utils/clipboard'

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
    },
    {
      title: '产品',
      dataIndex: 'description',
    },
    {
      title: '应用名',
      dataIndex: 'applicationName',
    },
    {
      title: 'Token',
      dataIndex: 'token',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '操作',
      dataIndex: 'operation',
      slots: { customRender: 'operation' },
    }
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
        applicationTokenForm: {
          applicationName: null,
          productId: 1,
        },
        visible: false,
        confirmLoading: false,
        productData: [],
      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
      showModal() {
        this.visible = true

        getProductList().then((res) => {
          this.productData = res.data
        })
      },
      addApplicationToken() {
        this.confirmLoading = true
        addApplication(this.applicationTokenForm)
          .then((res) => {
            this.visible = false
            this.fetch()
          })
          .finally(() => {
            this.confirmLoading = false
          })
      },
      handleCancel() {
        this.visible = false
      },
      handleTableChange(pagination) {
        const pager = {...this.pagination}
        pager.current = pagination.current
        this.pagination = pager
        this.fetch()
      },
      onCopyApplicationToken(event, record) {
        let data = record.token
        copyHandle(data)
      },
      _deleteApplicationOne(record) {
        deleteApplicationToken(record.id).then(res => {
          this.fetch()
        })
      },
      fetch() {
        this.loading = true
        getPage({
          pageSize: this.pagination.pageSize,
          current: this.pagination.current,
        }).then((res) => {
          console.log(res)
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
