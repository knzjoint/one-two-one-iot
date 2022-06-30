<template>
  <div>
    <a-button type="primary" @click="showModal">创建应用</a-button>
    <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :data-source="data"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template
        #filterDropdown="{ setSelectedKeys, selectedKeys, confirm, column }"
      >
        <div ref="searchInput" style="padding: 8px">
          <a-input
            :placeholder="请输入产品英文名称"
            :value="selectedKeys[0]"
            style="width: 188px; margin-bottom: 8px; display: block"
            @change="
              (e) => setSelectedKeys(e.target.value ? [e.target.value] : [])
            "
            @pressEnter="handleSearch(selectedKeys, confirm, column.dataIndex)"
          />
          <a-button
            type="primary"
            size="small"
            style="width: 90px; margin-right: 8px"
            @click="handleSearch(selectedKeys, confirm, column.dataIndex)"
          >
            <template #icon><SearchOutlined /></template>
            查找
          </a-button>
          <a-button size="small" style="width: 90px" @click="handleReset()">
            重置
          </a-button>
        </div>
      </template>
      <template #filterIcon="filtered">
        <search-outlined :style="{ color: filtered ? '#108ee9' : undefined }" />
      </template>
      <template #operation="{ record }">
        <a-button style="margin-right: 8px" size="small" type="primary" @click="showProductTopic(record)">
          主题
        </a-button>
        <a-popconfirm
            title="是否删除此产品?"
            @confirm="_deleteProduct(record)"
        >
          <a-button size="small" type="primary">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>

    <a-modal
      title="创建产品"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="addApplicationToken"
      @cancel="handleCancel"
    >
      <a-form
        :model="productForm"
        :label-col="{ span: 4 }"
        :rules="productFormRules"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="产品名"  name="productName">
          <a-input
            v-model:value="productForm.productName"
            placeholder="产品名"
          />
        </a-form-item>
        <a-form-item label="英文名" name="productEnName">
          <a-input
            v-model:value="productForm.productEnName"
            placeholder="英文名"
          />
        </a-form-item>
        <a-form-item label="描述" name="productDesc">
          <a-textarea v-model:value="productForm.productDesc" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
  import { getPage, getProductList, addProduct, deleteProduct } from '@/api/product'
  import { SearchOutlined } from '@ant-design/icons-vue'

  export default {
    components: {
      SearchOutlined,
    },
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
        productForm: {
          productName: undefined,
          productEnName: undefined,
          productDesc: undefined,
        },
        productFormRules: {
          productName: [
            { required: true, message: '请输入产品名称!', trigger: 'change' },
          ],
          productEnName: [
            { required: true, validator: this.validateProductName, trigger: 'change' }
          ],
        },
        visible: false,
        confirmLoading: false,
        productData: [],
        columns: [
          {
            title: '产品名',
            dataIndex: 'productName',
          },
          {
            title: '产品英文名',
            dataIndex: 'productEnName',
            slots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
            },
          },
          {
            title: '产品描述',
            dataIndex: 'productDesc',
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
        ],
      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
      async validateProductName(_rule, value) {
        // 用户名正则，8到16位（字母，数字，下划线，减号, $）
        const reg = /^[a-zA-Z][a-zA-Z0-9_]{6,15}$/
        // 输出 true
        if (!reg.test(value)) {
          return Promise.reject('以字母开头，6到16位（字母，数字，下划线，减号, $）')
        }
        return Promise.resolve()
      },
      showProductTopic(record) {
        this.$router.push({ name: 'productTopics', params: { id: record.id, productEnName: record.productEnName }})
      },
      handleSearch(selectedKeys) {
        this.fetch({ productEnName: selectedKeys })
      },
      handleReset() {
        this.fetch()
      },
      showModal() {
        this.visible = true

        getProductList().then((res) => {
          this.productData = res.data
        })
      },
      addApplicationToken() {
        this.confirmLoading = true
        addProduct(this.productForm)
          .then(() => {
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
        const pager = { ...this.pagination }
        pager.current = pagination.current
        this.pagination = pager
        this.fetch()
      },
      _deleteProduct(record) {
        deleteProduct(record.id).then(res => {
          this.fetch()
        })
      },
      fetch(filters) {
        this.loading = true
        getPage({
          pageSize: this.pagination.pageSize,
          current: this.pagination.current,
          ...filters,
        }).then((res) => {
          this.loading = false
          // const pagination = { ...this.pagination }
          this.pagination.total = res.data.total
          // this.loading = false
          this.data = res.data.records
          // this.pagination = pagination
          // this.$refs.searchInput.style.display = 'none'
        })
      },
    },
  }
</script>
