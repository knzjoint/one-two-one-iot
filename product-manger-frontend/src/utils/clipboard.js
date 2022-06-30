import Clipboard from 'clipboard'
import { message } from 'ant-design-vue'

function clipboardSuccess(text) {
  message.success(`复制${text}成功`)
}

function clipboardError(text) {
  message.error(`复制${text}失败`)
}

/**
 * @description 复制数据
 * @param text
 * @param event
 */
export function handleClipboard(text, event) {
  const clipboard = new Clipboard(event.target, {
    text: () => text,
  })
  clipboard.on('success', () => {
    clipboardSuccess(text)
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError(text)
    clipboard.destroy()
  })
  clipboard.onClick(event)
}

export function handleClipboardText(text) {
  let copy = (e) => {
    e.preventDefault()
    e.clipboardData.setData('text/plain', text)
    console.log(e)
    handleClipboard(text, e)
    document.removeEventListener('copy', copy)
  }
  document.addEventListener('copy', copy)
  document.execCommand('copy')
  document.createAttribute('data-clipboard-text')
}

export function copyHandle(text) {
  let copy = (e) => {
    e.preventDefault()
    e.clipboardData.setData('text/plain', text)
    clipboardSuccess(text)
    document.removeEventListener('copy', copy)
  }
  document.addEventListener('copy', copy)
  document.execCommand('copy')
}
