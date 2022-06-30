<template><h1 id="连接pulsar" tabindex="-1"><a class="header-anchor" href="#连接pulsar" aria-hidden="true">#</a> 连接Pulsar</h1>
<h3 id="安装库pulsar-client" tabindex="-1"><a class="header-anchor" href="#安装库pulsar-client" aria-hidden="true">#</a> 安装库pulsar-client</h3>
<p>增加下载速度</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>repositories {
    maven { url 'https://maven.aliyun.com/repository/public/' }
    maven { url 'https://maven.aliyun.com/repository/google/'}
    maven { url 'https://maven.aliyun.com/repository/jcenter/'}
    mavenCentral()
}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br></div></div><div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>def pulsarVersion = '2.9.1'

dependencies {
    compile group: 'org.apache.pulsar', name: 'pulsar-client', version: pulsarVersion
}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br></div></div></template>
