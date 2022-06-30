<template>
  <div>
    <a-button type="primary" @click="showAddTopic">创建主题</a-button>
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
        <div v-if="record.operatorAuth">
          <a-popconfirm
              title="是否删除此主题?"
              @confirm="_deleteTopicOne(record)"
          >
            <a-button size="small" type="primary">删除</a-button>
          </a-popconfirm>
          <a-button size="small" type="primary" style="margin-left: 8px" @click="showEditTopic(record)">编辑</a-button>
        </div>
      </template>
    </a-table>

    <a-modal
      title="添加主题"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleTopic"
      @cancel="visible = false"
    >
      <a-form
        :model="topicForm"
        :label-col="{ span: 4 }"
        :rules="topicFormRules"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="主题名" name="topic">
          <a-input :addon-before="productEnName" v-model:value="topicForm.topic" placeholder="主题名" />
        </a-form-item>
        <a-form-item label="访问权限" name="access">
          <a-select :value="topicForm.access" @change="accessChange">
            <a-select-option :value="1" :key="1">订阅</a-select-option>
            <a-select-option :value="2" :key="2">发布</a-select-option>
            <a-select-option :value="3" :key="3">订阅/发布</a-select-option>
          </a-select>
        </a-form-item>
        <!--<a-form-item label="IP限制">-->
          <!--<a-input-group size="large">-->
            <!--<a-row :gutter="8">-->
              <!--<a-col :span="5">-->
                <!--<a-input default-value="0571" />-->
              <!--</a-col>-->
              <!--<a-col :span="8">-->
                <!--<a-input default-value="26888888" />-->
              <!--</a-col>-->
            <!--</a-row>-->
          <!--</a-input-group>-->
          <!--<a-input-->
            <!--v-model:value="topicForm.ipaddr"-->
            <!--placeholder="IP限制"-->
          <!--/>-->
        <!--</a-form-item>-->
        <a-form-item label="描述" name="topicDesc">
          <a-textarea v-model:value="topicForm.topicDesc" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
  import { getTopicList, addTopic, deleteTopicOne, updateTopic } from '@/api/productTopic'
  import { SearchOutlined } from '@ant-design/icons-vue'

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
    components: {
      SearchOutlined,
    },
    data() {
      return {
        data: [],
        query: {},
        loading: false,
        topicForm: {
          topic: undefined,
          access: 1,
          // ipaddr: undefined,
          topicDesc: undefined,
        },
        topicFormRules: {
          topic: [
            { required: true, message: '请输入主题名称!', trigger: 'blur' },
            { required: true, validator: this.validateTopic, trigger: 'change' }
          ]
        },
        productEnName: '',
        visible: false,
        isEdit: false,
        confirmLoading: false,
        productData: [],
        columns,
      }
    },
    mounted() {
      this.productEnName = '/' + this.$route.params.productEnName + '/{device}/'
      this.fetch()
    },
    methods: {
      validateTopic(_rule, value) {
        // 用户名正则，8到16位（字母，数字，下划线，减号, $）
        const reg = /^[a-zA-Z][a-zA-Z0-9_]{1,100}$/
        // 输出 true
        if (!reg.test(value)) {
          return Promise.reject('以字母开头，字母，数字，下划线，减号, $')
        }
        return Promise.resolve()
      },
      handleSearch(selectedKeys) {
        this.fetch({ productEnName: selectedKeys })
      },
      handleReset() {
        this.fetch()
      },
      showAddTopic() {
        this.isEdit = false
        this.visible = true
      },
      accessChange(value) {
        this.topicForm.access = value
      },
      showEditTopic(record) {
        this.visible = true
        this.isEdit = true
        this.topicForm = { ...record }
        if (this.topicForm.topic !== null && this.topicForm.topic !== undefined) {
          this.topicForm.topic = this.topicForm.topic.match(/{device}\/(\S*)/) === undefined || this.topicForm.topic.match(/{device}\/(\S*)/) === null ?
            this.topicForm.topic : this.topicForm.topic.match(/{device}\/(\S*)/)[1]
        }
      },
      handleTopic() {
        if (this.isEdit) {
          this._updateTopic()
        } else {
          this._addTopic()
        }
      },
      fetch(filters) {
        this.loading = true
        getTopicList(this.$route.params.id).then((res) => {
          this.data = res.data
          this.loading = false
        })
      },
      _addTopic() {
        this.topicForm.productId = this.$route.params.id
        addTopic(this.topicForm).then((res) => {
          this.fetch()
          this.$message.success(res.msg)
        }).catch((err) => {
          this.fetch()
          this.$message.warning(err.msg)
        })
      },
      _updateTopic() {
        updateTopic(this.topicForm.id, this.topicForm).then((res) => {
          this.fetch()
          this.$message.success(res.msg)
          this.visible = false
        }).catch((err) => {
          this.$message.warning(err.msg)
        })
      },
      _deleteTopicOne(record) {
        deleteTopicOne(record.id).then((res) => {
          this.fetch()
          this.$message.success(res.msg)
        }).catch((err) => {
          this.$message.warning(err.msg)
        })
      },
    },
  }
</script>
