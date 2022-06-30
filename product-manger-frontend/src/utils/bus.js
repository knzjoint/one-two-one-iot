import mitt from 'mitt';
const mitter = mitt();

const bus = {
  $emit: mitter.emit,
  $on: mitter.on,
  $off: mitter.off
}

export {
  bus
}
