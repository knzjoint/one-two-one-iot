<template>
  <div v-if="controlUi.controlType === 'BUTTON'">
    <a-button size="small" @click="handleClick">{{ dataFormat.trueText }}</a-button>
  </div>
  <div v-if="controlUi.controlType === 'INPUT'">
    <a-input v-model:value="inputValue"></a-input>
    <a-button @click="handleClick">{{ controlUi.name }}</a-button>
  </div>
  <div v-if="controlUi.controlType === 'SLIDER'">

    <a-space :size="10">
      <div>{{ controlUi.name }} :</div>
      <div style="width: 80px">
        <a-slider @change="handleSliderChange" v-model:value="inputValue" :min="dataFormat.max" :max="dataFormat.min" :step="dataFormat.step" />
      </div>
    </a-space>
  </div>

  <div v-if="controlUi.controlType === 'SWITCH'">
    <a-space :size="10">
      <div>{{ controlUi.name }} :</div>
      <a-switch @click="handleSwitchChange" :checked-children="dataFormat.trueText" :un-checked-children="dataFormat.falseText" v-model:checked="switchValue" />
    </a-space>
  </div>

  <div v-if="controlUi.controlType === 'TEXT_INPUT'">
    <a-textarea v-model:value="inputValue"></a-textarea>
    <a-button size="small" @click="handleClick">{{ controlUi.name }}</a-button>
  </div>

  <div v-if="controlUi.controlType === 'JSON_INPUT'">
    <!--<a-textarea v-model:value="inputValue"></a-textarea>-->
    <!--<v-ace-editor v-model:value="inputValue" lang="json" :options="{ useWorker: true }" />-->
    <Vue3JsonEditor
        v-model="inputValue"
        :show-btns="false"
        :mode="'text'"
        :expandedOnStart="true"
    />
    <a-button size="small" @click="handleClick">{{ controlUi.name }}</a-button>
  </div>
</template>

<script>
  import { controlThings } from '@/api/things'
  import { Vue3JsonEditor } from 'vue3-json-editor'

  export default {
    name: "int-data-format",
    components: {
      Vue3JsonEditor,
    },
    props: {
      thingsId: {
        type: Number,
        default: -1,
      },
      controlUi: {
        type: Object,
        default: () => {}
      },
      dataFormat: {
        type: Object,
        default: () => {},
      },
      value: 0,
    },
    data() {
      return {
        switchValue: 1,
        controlDeviceForm: {},
        inputValue: undefined,
      }
    },
    watch: {
      controlUi(newVal) {
        this.updateValue()
      }
    },
    mounted() {
      this.controlDeviceForm.thingsId = this.thingsId
      this.controlDeviceForm.type = this.dataFormat.type
      this.updateValue()
    },
    methods: {
      updateValue() {
        if (this.controlUi.controlType === 'BUTTON') {
        } else if (this.controlUi.controlType === 'INPUT') {
          this.inputValue = this.value
        } else if (this.controlUi.controlType === 'SLIDER') {
          this.inputValue = this.value
        } else if (this.controlUi.controlType === 'SWITCH') {
          this.switchValue = this.value === 'true' ? true : false
        } else if (this.controlUi.controlType === 'TEXT_INPUT') {
          this.inputValue = this.value
        } else if (this.controlUi.controlType === 'JSON_INPUT') {
          this.inputValue = JSON.parse(this.value)
        }
      },
      handleClick() {
        this.controlDeviceForm.value = this.inputValue
        this._controlThings()
      },
      handleSwitchChange(value) {
        this.controlDeviceForm.value = value
        this._controlThings()
      },
      handleSliderChange(value) {
        this.controlDeviceForm.value = value
        this._controlThings()
      },
      _controlThings() {
        controlThings(this.controlDeviceForm).then(res => {
          this.$message.success(res.msg)
        }).catch((err) => {
          this.$message.warning(err.msg)
        })
      },
    }
  }
</script>

<style>

  div.jsoneditor-menu {
    display: none;
  }

</style>
