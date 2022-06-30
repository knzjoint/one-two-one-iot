<template>
  <div>
    <a-button type="primary" @click="showModal">添加场景联动</a-button>
    <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :data-source="data"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template #isStartup="{ text: isStartup }">
        <a-tag color="green" v-if="isStartup">启动</a-tag>
        <a-tag color="red" v-else>关闭</a-tag>
      </template>
      <template #operation="{ record }">
        <a-button style="margin-right: 8px" size="small" type="primary" @click="jumpToLinkageEdit(record)">
          场景联动编辑
        </a-button>
        <a-popconfirm
            title="是否删除此产品?"
            @confirm="_deleteLinkage(record)"
        >
          <a-button size="small" type="primary">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>

    <a-modal
      title="创建产品"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="_addRules"
      @cancel="handleCancel"
    >
      <a-form
        :model="thingsLinkageForm"
        :label-col="{ span: 4 }"
        :rules="thingsLinkageFormRules"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="联动场景名称"  name="name">
          <a-input
            v-model:value="thingsLinkageForm.name"
            placeholder="联动场景名称"
          />
        </a-form-item>

        <a-form-item name="isStartup" label="是否启动">
          <a-switch checked-children="启动" un-checked-children="关闭" v-model:checked="thingsLinkageForm.isStartup"/>
        </a-form-item>

        <a-form-item label=" 产 品 ">
          <a-select style="width: 120px" v-model:value="thingsLinkageForm.venuesId">
            <a-select-option
                v-for="item in venuesSelectData"
                :value="item.value"
                :key="item.value"
            >
              {{ item.text }}
            </a-select-option>
          </a-select>
        </a-form-item>


        <a-form-item label="联动场景描述" name="linkageDesc">
          <a-textarea v-model:value="thingsLinkageForm.linkageDesc" />
        </a-form-item>

      </a-form>
    </a-modal>
  </div>
</template>
<script>
  import { getVenuesList } from '@/api/venues'
  import { getVenuesThingsLinkage, addRules, deleteLinkage } from '@/api/thingsLinkage'
  import { SearchOutlined } from '@ant-design/icons-vue'

  export default {
    components: {
      SearchOutlined,
    },
    data() {
      return {
        venuesId: -1,
        venuesSelectData: [],
        data: [],
        pagination: {
          showLessItems: true,
          showQuickJumper: true,
          showSizeChanger: true,
        },
        query: {},
        loading: false,
        thingsLinkageForm: {
          name: undefined,
          isStartup: true,
          linkageDesc: undefined,
        },
        thingsLinkageFormRules: {
          name: [
            { required: true, message: '请输入联动场景名称!', trigger: 'change' },
          ],
        },
        visible: false,
        confirmLoading: false,
        columns: [
          {
            title: '联动场景名称',
            dataIndex: 'name',
          },
          {
            title: '是否启动',
            dataIndex: 'isStartup',
            slots: { customRender: 'isStartup' },
            filters: [
              { text: '启动', value: true },
              { text: '关闭', value: false },
            ],
          },
          {
            title: '联动场景描述',
            dataIndex: 'linkageDesc',
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
      this.venuesId = this.$route.query.venuesId
      this.thingsLinkageForm.venuesId = this.venuesId

      this.fetch({ venuesId: this.venuesId })
      this._getVenuesList()
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
      jumpToLinkageEdit(record) {
        this.$router.push({ name: 'WorkFlow', query: { id: record.id, venuesId: record.venuesId }})
      },
      handleSearch(selectedKeys) {
        this.fetch({ productEnName: selectedKeys })
      },
      handleReset() {
        this.fetch()
      },
      showModal() {
        this.visible = true
      },
      _addRules() {
        this.confirmLoading = true

        addRules(this.thingsLinkageForm)
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
      _deleteLinkage(record) {
        deleteLinkage(record.id).then(res => {
          this.fetch()
        })
      },

      _getVenuesList() {
        getVenuesList().then((res) => {
          this.venuesSelectData = res.data.map(e => {
            return { text: e.venuesName, value: e.id }
          })
        })
      },

      fetch(filters) {
        this.loading = true
        getVenuesThingsLinkage({
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
