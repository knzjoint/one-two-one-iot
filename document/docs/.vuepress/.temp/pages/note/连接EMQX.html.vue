<template><h1 id="连接emq-x" tabindex="-1"><a class="header-anchor" href="#连接emq-x" aria-hidden="true">#</a> 连接EMQ X</h1>
<h2 id="简介" tabindex="-1"><a class="header-anchor" href="#简介" aria-hidden="true">#</a> 简介</h2>
<p>从EMQ X中获取消息的方式比较多</p>
<ul>
<li>
<p>购买EMQ X的企业版，包括很多消息流转模块；</p>
</li>
<li>
<p>创建个EMQ X超级用户，订阅所有的消息事件；</p>
</li>
<li>
<p>使用规则引擎；</p>
</li>
<li>
<p>移植EMQ X的kafaka插件；</p>
</li>
<li>
<p>通过WebHook插件获取消息。</p>
<p><img src="http://note.makerknz.cn/image-20210728134815313.png" alt="image-20210728134815313"></p>
</li>
</ul>
<p>本文通过WebHook插件获取消息。设备的所有事件会通过webhook发送到产品服务器。</p>
<p><img src="http://note.makerknz.cn/image-20211121090519234.png" alt="image-20211121090519234"></p>
<h2 id="webhook介绍" tabindex="-1"><a class="header-anchor" href="#webhook介绍" aria-hidden="true">#</a> <a href="https://docs.emqx.cn/broker/v4.3/advanced/webhook.html" target="_blank" rel="noopener noreferrer">WebHook介绍<ExternalLinkIcon/></a></h2>
<p>WebHook 是由 <a href="https://github.com/emqx/emqx-web-hook" target="_blank" rel="noopener noreferrer">emqx_web_hook (opens new window)<ExternalLinkIcon/></a>插件提供的 将 EMQ X 中的钩子事件通知到某个 Web 服务的功能。</p>
<p>可以理解为EMQ X创建了一个客户端，这个客户端可以收集设备的在线、下下线记录、订阅与消息存储、消息送达确认等事件消息，通过钩子上挂载回调函数将事件发送到web器。</p>
<div class="language-bash ext-sh line-numbers-mode"><pre v-pre class="language-bash"><code>    Client      <span class="token operator">|</span>    EMQ X     <span class="token operator">|</span>  emqx_web_hook <span class="token operator">|</span>   HTTP       +------------+
  <span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">=</span><span class="token operator">>|</span> - - - - - - -<span class="token operator">></span> - - - - - - - -<span class="token operator">>=</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">==</span><span class="token operator">></span>  <span class="token operator">|</span> Web Server <span class="token operator">|</span>
                <span class="token operator">|</span>    Broker    <span class="token operator">|</span>                <span class="token operator">|</span>  Request     +------------+
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br></div></div><div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>WebHook消息是单向的。
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><h3 id="webhook配置" tabindex="-1"><a class="header-anchor" href="#webhook配置" aria-hidden="true">#</a> Webhook配置</h3>
<h3 id="web-hook-url" tabindex="-1"><a class="header-anchor" href="#web-hook-url" aria-hidden="true">#</a> web.hook.url</h3>
<table>
<thead>
<tr>
<th>Type</th>
<th>Value</th>
</tr>
</thead>
<tbody>
<tr>
<td>string</td>
<td>http://192.168.31.216:9200/emqx/webhook</td>
</tr>
</tbody>
</table>
<p>Webhook 请求转发的目的 Web 服务器地址。</p>
<h3 id="web-hook-headers" tabindex="-1"><a class="header-anchor" href="#web-hook-headers" aria-hidden="true">#</a> web.hook.headers</h3>
<div class="language-properties ext-properties line-numbers-mode"><pre v-pre class="language-properties"><code><span class="token attr-name">web.hook.headers.content-type</span> <span class="token punctuation">=</span> <span class="token attr-value">application/json</span>
<span class="token attr-name">web.hook.headers.accept</span> <span class="token punctuation">=</span> <span class="token attr-value">*/*</span>
<span class="token attr-name">web.hook.headers.webhook-username</span> <span class="token punctuation">=</span> <span class="token attr-value">makerknz</span>
<span class="token attr-name">web.hook.headers.webhook-password</span> <span class="token punctuation">=</span> <span class="token attr-value">123456</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br></div></div><p>指定 HTTP 请求头部中的数据。<code>&lt;Key&gt;</code> 指定 HTTP 请求头部中的字段名，此配置项的值为相应的字段值。<code>&lt;Key&gt;</code> 可以是标准的 HTTP 请求头部字段，也可以自定义的字段，可以配置多个不同的请求头部字段。</p>
<p>webhook-username和webhook-password 作为访问后端的凭据，当emq x是一个集群时用来区别不同的服务器。当然认证也可以通过添加证书进行SSL认证，这部分感兴趣的同学可以自己探索。</p>
<h2 id="触发规则" tabindex="-1"><a class="header-anchor" href="#触发规则" aria-hidden="true">#</a> 触发规则</h2>
<p>在 <code>etc/plugins/emqx_web_hooks.conf</code> 可配置触发规则，其配置的格式如下：</p>
<div class="language-bash ext-sh line-numbers-mode"><pre v-pre class="language-bash"><code><span class="token comment">## 格式示例</span>
web.hook.rule.<span class="token operator">&lt;</span>Event<span class="token operator">></span>.<span class="token operator">&lt;</span>Number<span class="token operator">></span> <span class="token operator">=</span> <span class="token operator">&lt;</span>Rule<span class="token operator">></span>

<span class="token comment">## 示例值</span>
web.hook.rule.message.publish.1 <span class="token operator">=</span> <span class="token punctuation">{</span><span class="token string">"action"</span><span class="token builtin class-name">:</span> <span class="token string">"on_message_publish"</span>, <span class="token string">"topic"</span><span class="token builtin class-name">:</span> <span class="token string">"a/b/c"</span><span class="token punctuation">}</span>
web.hook.rule.message.publish.2 <span class="token operator">=</span> <span class="token punctuation">{</span><span class="token string">"action"</span><span class="token builtin class-name">:</span> <span class="token string">"on_message_publish"</span>, <span class="token string">"topic"</span><span class="token builtin class-name">:</span> <span class="token string">"foo/#"</span><span class="token punctuation">}</span>
web.hook.rule.message.publish.2 <span class="token operator">=</span> <span class="token punctuation">{</span><span class="token string">"action"</span><span class="token builtin class-name">:</span> <span class="token string">"on_message_publish"</span><span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br></div></div><p>项目中会设置全部转发到后端服务器，不设置topic过滤。</p>
<h3 id="event-触发事件" tabindex="-1"><a class="header-anchor" href="#event-触发事件" aria-hidden="true">#</a> Event 触发事件</h3>
<p>目前支持以下事件：</p>
<table>
<thead>
<tr>
<th>名称</th>
<th>说明</th>
<th>执行时机</th>
</tr>
</thead>
<tbody>
<tr>
<td>client.connect</td>
<td>处理连接报文</td>
<td>服务端收到客户端的连接报文时</td>
</tr>
<tr>
<td>client.connack</td>
<td>下发连接应答</td>
<td>服务端准备下发连接应答报文时</td>
</tr>
<tr>
<td>client.connected</td>
<td>成功接入</td>
<td>客户端认证完成并成功接入系统后</td>
</tr>
<tr>
<td>client.disconnected</td>
<td>连接断开</td>
<td>客户端连接层在准备关闭时</td>
</tr>
<tr>
<td>client.subscribe</td>
<td>订阅主题</td>
<td>收到订阅报文后，执行 <code>client.check_acl</code> 鉴权前</td>
</tr>
<tr>
<td>client.unsubscribe</td>
<td>取消订阅</td>
<td>收到取消订阅报文后</td>
</tr>
<tr>
<td>session.subscribed</td>
<td>会话订阅主题</td>
<td>完成订阅操作后</td>
</tr>
<tr>
<td>session.unsubscribed</td>
<td>会话取消订阅</td>
<td>完成取消订阅操作后</td>
</tr>
<tr>
<td>message.publish</td>
<td>消息发布</td>
<td>服务端在发布（路由）消息前</td>
</tr>
<tr>
<td>message.delivered</td>
<td>消息投递</td>
<td>消息准备投递到客户端前</td>
</tr>
<tr>
<td>message.acked</td>
<td>消息回执</td>
<td>服务端在收到客户端发回的消息 ACK 后</td>
</tr>
<tr>
<td>message.dropped</td>
<td>消息丢弃</td>
<td>发布出的消息被丢弃后</td>
</tr>
</tbody>
</table>
<h3 id="number" tabindex="-1"><a class="header-anchor" href="#number" aria-hidden="true">#</a> Number</h3>
<p>同一个事件可以配置多个触发规则，配置相同的事件应当依次递增。</p>
<h3 id="rule" tabindex="-1"><a class="header-anchor" href="#rule" aria-hidden="true">#</a> Rule</h3>
<p>触发规则，其值为一个 JSON 字符串，其中可用的 Key 有：</p>
<ul>
<li>action：字符串，取固定值</li>
<li>topic：字符串，表示一个主题过滤器，操作的主题只有与该主题匹配才能触发事件的转发</li>
</ul>
<p>例如，我们只将与 <code>a/b/c</code> 和 <code>foo/#</code> 主题匹配的消息转发到 Web 服务器上，其配置应该为：</p>
<div class="language-bash ext-sh line-numbers-mode"><pre v-pre class="language-bash"><code>web.hook.rule.message.publish.1 <span class="token operator">=</span> <span class="token punctuation">{</span><span class="token string">"action"</span><span class="token builtin class-name">:</span> <span class="token string">"on_message_publish"</span>, <span class="token string">"topic"</span><span class="token builtin class-name">:</span> <span class="token string">"a/b/c"</span><span class="token punctuation">}</span>
web.hook.rule.message.publish.2 <span class="token operator">=</span> <span class="token punctuation">{</span><span class="token string">"action"</span><span class="token builtin class-name">:</span> <span class="token string">"on_message_publish"</span>, <span class="token string">"topic"</span><span class="token builtin class-name">:</span> <span class="token string">"foo/#"</span><span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br></div></div><p>这样 Webhook 仅会转发与 <code>a/b/c</code> 和 <code>foo/#</code> 主题匹配的消息，例如 <code>foo/bar</code> 等，而不是转发 <code>a/b/d</code> 或 <code>fo/bar</code>。</p>
<h2 id="webflux-介绍" tabindex="-1"><a class="header-anchor" href="#webflux-介绍" aria-hidden="true">#</a> WebFlux 介绍</h2>
<p>WebFlux是Spring中的异步非阻塞的响应式web框架，EMQ X的设备有12种事件类型，对于单机安装的<a href="https://blog.csdn.net/weixin_44455388/article/details/108714963" target="_blank" rel="noopener noreferrer">EMQ X并发连接11万<ExternalLinkIcon/></a>会产品webhook的请求，后端服务器是同步MVC会导致请求堆积，使服务处理逻辑。</p>
<p>如果通过web.hook.pool_size 配置连接池数，又会导致数据的延迟。</p>
<p><img src="http://5b0988e595225.cdn.sohucs.com/images/20200507/1a6c626d50df48b8a2109f790ec2b4dc.jpeg" alt="img"></p>
<p>MVC JDBC是一个同步请求的过程，每一次请求都会在一个线程内执行，等一次请求完成之后才能释放资源，如果在service方法中业务逻辑如果碰到io操作时间比较长的操作，这样这个service方法就会长时间占用tomcat容器线程池中的线程，这样是不利于其他请求的处理的，当线程池中的线程处理任务时，任务由于长时间io操作，肯定会阻塞线程处理其他任务。</p>
<p>WebFlux R2DBC是一个异步非阻塞的请求过程，此处会涉及大量的EMQX的请求，如果请求堆积，可以通过设置背压调整每次处理的数量来达到对请求的削峰处理。</p>
<h2 id="webhook接口设计" tabindex="-1"><a class="header-anchor" href="#webhook接口设计" aria-hidden="true">#</a> ## WebHook接口设计</h2>
<h3 id="增加webflux接口" tabindex="-1"><a class="header-anchor" href="#增加webflux接口" aria-hidden="true">#</a> 增加WebFlux接口</h3>
<h4 id="添加依赖包" tabindex="-1"><a class="header-anchor" href="#添加依赖包" aria-hidden="true">#</a> 添加依赖包</h4>
<p>添加包WebFlux使用的包。r2dbc-mysql用来连接数据库。</p>
<div class="language-groovy ext-groovy line-numbers-mode"><pre v-pre class="language-groovy"><code>    <span class="token function">implementation</span><span class="token punctuation">(</span><span class="token string">'org.springframework.boot:spring-boot-starter-webflux'</span><span class="token punctuation">)</span>
    <span class="token function">implementation</span><span class="token punctuation">(</span><span class="token string">'org.springframework.boot:spring-boot-starter-data-r2dbc'</span><span class="token punctuation">)</span>
    <span class="token function">implementation</span><span class="token punctuation">(</span><span class="token string">'dev.miku:r2dbc-mysql'</span><span class="token punctuation">)</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br></div></div><h4 id="配置" tabindex="-1"><a class="header-anchor" href="#配置" aria-hidden="true">#</a> 配置</h4>
<div class="language-yaml ext-yml line-numbers-mode"><pre v-pre class="language-yaml"><code><span class="token key atrule">spring</span><span class="token punctuation">:</span>
  <span class="token key atrule">r2dbc</span><span class="token punctuation">:</span>
    <span class="token key atrule">url</span><span class="token punctuation">:</span> r2dbcs<span class="token punctuation">:</span>mysql<span class="token punctuation">:</span>//www.makerknz.cn<span class="token punctuation">:</span>3306/product_manager<span class="token punctuation">?</span>characterEncoding=utf<span class="token punctuation">-</span>8<span class="token important">&amp;useSSL=false</span>
    <span class="token key atrule">username</span><span class="token punctuation">:</span> product_manager
    <span class="token key atrule">password</span><span class="token punctuation">:</span> <span class="token number">123456</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br></div></div><h4 id="添加repository" tabindex="-1"><a class="header-anchor" href="#添加repository" aria-hidden="true">#</a> 添加repository</h4>
<p>WebHook目前设计只对外开放一个接口，仅用到device_events和device两张表，所以只写这两张表的repository即可。</p>
<p>DeviceEventsRepository</p>
<div class="language-java ext-java line-numbers-mode"><pre v-pre class="language-java"><code><span class="token keyword">package</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>repository</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>entity<span class="token punctuation">.</span></span><span class="token class-name">DeviceEvents</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>data<span class="token punctuation">.</span>repository<span class="token punctuation">.</span>reactive<span class="token punctuation">.</span></span><span class="token class-name">ReactiveCrudRepository</span><span class="token punctuation">;</span>

<span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">DeviceEventsRepository</span> <span class="token keyword">extends</span> <span class="token class-name">ReactiveCrudRepository</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">DeviceEvents</span><span class="token punctuation">,</span> <span class="token class-name">Integer</span><span class="token punctuation">></span></span> <span class="token punctuation">{</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br></div></div><p>DeviceRepository</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.repository;

import cn.makerknz.product.server.entity.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeviceRepository extends ReactiveCrudRepository&lt;Device, Integer> {

    Mono&lt;Device> findByClientId(String clientId);

}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br></div></div><h4 id="service实现" tabindex="-1"><a class="header-anchor" href="#service实现" aria-hidden="true">#</a> service实现</h4>
<p>在DeviceEventsServiceImpl增加实现</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.DeviceEvents;
import cn.makerknz.product.server.mapper.DeviceEventsMapper;
import cn.makerknz.product.server.repository.DeviceEventsRepository;
import cn.makerknz.product.server.service.IDeviceEventsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * &lt;p>
 *  服务实现类
 * &lt;/p>
 *
 * @author maker knz
 * @since 2021-10-27
 */

@Service
public class DeviceEventsServiceImpl extends ServiceImpl&lt;DeviceEventsMapper, DeviceEvents> implements IDeviceEventsService {

    @Autowired
    private DeviceEventsRepository deviceEventsRepository;

    @Override
    public Mono&lt;DeviceEvents> add(DeviceEvents deviceEvents) {
        return deviceEventsRepository.save(deviceEvents);
    }

}

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br><span class="line-number">32</span><br><span class="line-number">33</span><br></div></div><p>在DeviceServiceImpl中增加实现</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.service.impl;

import cn.makerknz.product.server.entity.Device;
import cn.makerknz.product.server.mapper.DeviceMapper;
import cn.makerknz.product.server.repository.DeviceRepository;
import cn.makerknz.product.server.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * &lt;p>
 *  服务实现类
 * &lt;/p>
 *
 * @author maker knz
 * @since 2021-10-27
 */
@Service
public class DeviceServiceImpl extends ServiceImpl&lt;DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Mono&lt;Device> findByClientId(String clientId) {
        return deviceRepository.findByClientId(clientId);
    }
}

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br></div></div><h4 id="对外接口" tabindex="-1"><a class="header-anchor" href="#对外接口" aria-hidden="true">#</a> 对外接口</h4>
<div class="language-java ext-java line-numbers-mode"><pre v-pre class="language-java"><code><span class="token keyword">package</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>controller</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">CheckEmqxWebhookIdentity</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>domain<span class="token punctuation">.</span>enums<span class="token punctuation">.</span></span><span class="token class-name">ResponseEnum</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>domain<span class="token punctuation">.</span>form<span class="token punctuation">.</span></span><span class="token class-name">EmqxWebhookForm</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>domain<span class="token punctuation">.</span>vo<span class="token punctuation">.</span></span><span class="token class-name">ResultVO</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>entity<span class="token punctuation">.</span></span><span class="token class-name">DeviceEvents</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>service<span class="token punctuation">.</span></span><span class="token class-name">IDeviceEventsService</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">cn<span class="token punctuation">.</span>makerknz<span class="token punctuation">.</span>product<span class="token punctuation">.</span>server<span class="token punctuation">.</span>service<span class="token punctuation">.</span></span><span class="token class-name">IDeviceService</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>beans<span class="token punctuation">.</span>factory<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">Autowired</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>web<span class="token punctuation">.</span>bind<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">PostMapping</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>web<span class="token punctuation">.</span>bind<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">RequestBody</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>web<span class="token punctuation">.</span>bind<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">RequestMapping</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">org<span class="token punctuation">.</span>springframework<span class="token punctuation">.</span>web<span class="token punctuation">.</span>bind<span class="token punctuation">.</span>annotation<span class="token punctuation">.</span></span><span class="token class-name">RestController</span><span class="token punctuation">;</span>
<span class="token keyword">import</span> <span class="token namespace">reactor<span class="token punctuation">.</span>core<span class="token punctuation">.</span>publisher<span class="token punctuation">.</span></span><span class="token class-name">Mono</span><span class="token punctuation">;</span>

<span class="token keyword">import</span> <span class="token namespace">java<span class="token punctuation">.</span>util<span class="token punctuation">.</span></span>UUID<span class="token punctuation">;</span>

<span class="token annotation punctuation">@RestController</span>
<span class="token annotation punctuation">@RequestMapping</span><span class="token punctuation">(</span><span class="token string">"/emqx"</span><span class="token punctuation">)</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">EmqxController</span> <span class="token punctuation">{</span>

    <span class="token annotation punctuation">@Autowired</span>
    <span class="token keyword">private</span> <span class="token class-name">IDeviceEventsService</span> deviceEventsService<span class="token punctuation">;</span>

    <span class="token annotation punctuation">@Autowired</span>
    <span class="token keyword">private</span> <span class="token class-name">IDeviceService</span> deviceService<span class="token punctuation">;</span>

    <span class="token annotation punctuation">@CheckEmqxWebhookIdentity</span>
    <span class="token annotation punctuation">@PostMapping</span><span class="token punctuation">(</span><span class="token string">"/webhook"</span><span class="token punctuation">)</span>
    <span class="token keyword">public</span> <span class="token class-name">Mono</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">ResultVO</span><span class="token punctuation">&lt;</span><span class="token class-name">Object</span><span class="token punctuation">></span><span class="token punctuation">></span></span> <span class="token function">webhook</span><span class="token punctuation">(</span><span class="token annotation punctuation">@RequestBody</span> <span class="token class-name">EmqxWebhookForm</span> emqxWebhookForm<span class="token punctuation">)</span> <span class="token punctuation">{</span>

        <span class="token keyword">return</span> deviceService<span class="token punctuation">.</span><span class="token function">findByClientId</span><span class="token punctuation">(</span>emqxWebhookForm<span class="token punctuation">.</span><span class="token function">getClientid</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">flatMap</span><span class="token punctuation">(</span>e <span class="token operator">-></span> <span class="token punctuation">{</span>
            <span class="token class-name">DeviceEvents</span> deviceEvents <span class="token operator">=</span> <span class="token class-name">DeviceEvents</span><span class="token punctuation">.</span><span class="token function">builder</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">productId</span><span class="token punctuation">(</span>e<span class="token punctuation">.</span><span class="token function">getProductId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">data</span><span class="token punctuation">(</span>emqxWebhookForm<span class="token punctuation">.</span><span class="token function">toString</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">deviceId</span><span class="token punctuation">(</span>e<span class="token punctuation">.</span><span class="token function">getId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">eventAction</span><span class="token punctuation">(</span>emqxWebhookForm<span class="token punctuation">.</span><span class="token function">getAction</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">streamId</span><span class="token punctuation">(</span>UUID<span class="token punctuation">.</span><span class="token function">randomUUID</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">toString</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">topic</span><span class="token punctuation">(</span>emqxWebhookForm<span class="token punctuation">.</span><span class="token function">getTopic</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">dataType</span><span class="token punctuation">(</span><span class="token number">1</span><span class="token punctuation">)</span>
                    <span class="token punctuation">.</span><span class="token function">build</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">return</span> deviceEventsService<span class="token punctuation">.</span><span class="token function">add</span><span class="token punctuation">(</span>deviceEvents<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">then</span><span class="token punctuation">(</span><span class="token class-name">Mono</span><span class="token punctuation">.</span><span class="token function">just</span><span class="token punctuation">(</span><span class="token class-name">ResultVO</span><span class="token punctuation">.</span><span class="token function">success</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">defaultIfEmpty</span><span class="token punctuation">(</span><span class="token class-name">ResultVO</span><span class="token punctuation">.</span><span class="token function">error</span><span class="token punctuation">(</span><span class="token class-name">ResponseEnum</span><span class="token punctuation">.</span>ERROR<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

<span class="token punctuation">}</span>

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br><span class="line-number">32</span><br><span class="line-number">33</span><br><span class="line-number">34</span><br><span class="line-number">35</span><br><span class="line-number">36</span><br><span class="line-number">37</span><br><span class="line-number">38</span><br><span class="line-number">39</span><br><span class="line-number">40</span><br><span class="line-number">41</span><br><span class="line-number">42</span><br><span class="line-number">43</span><br><span class="line-number">44</span><br><span class="line-number">45</span><br><span class="line-number">46</span><br><span class="line-number">47</span><br><span class="line-number">48</span><br></div></div><h2 id="webhook接口权限" tabindex="-1"><a class="header-anchor" href="#webhook接口权限" aria-hidden="true">#</a> Webhook接口权限</h2>
<h3 id="原因" tabindex="-1"><a class="header-anchor" href="#原因" aria-hidden="true">#</a> 原因</h3>
<p>webhook接口如果不做权限限制任何人都可以访问，可能会导致数据恶意添加。</p>
<h3 id="配置-1" tabindex="-1"><a class="header-anchor" href="#配置-1" aria-hidden="true">#</a> 配置</h3>
<p>对于分布式的EMQ X 每台可以配置不同的账户和密码</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>emqx:
  webhook:
    users:
      makerknz: 1234567
      makerknz_1: 12313123
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br></div></div><h3 id="读取配置" tabindex="-1"><a class="header-anchor" href="#读取配置" aria-hidden="true">#</a> 读取配置</h3>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "emqx.webhook")
@Data
public class EmqxConfig {

    private Map&lt;String,String> users;

}

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br></div></div><h3 id="增加切面注解" tabindex="-1"><a class="header-anchor" href="#增加切面注解" aria-hidden="true">#</a> 增加切面注解</h3>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface CheckEmqxWebhookIdentity {
}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br></div></div><h3 id="webhook接口认证实现" tabindex="-1"><a class="header-anchor" href="#webhook接口认证实现" aria-hidden="true">#</a> Webhook接口认证实现</h3>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>package cn.makerknz.product.server.auth;

import cn.makerknz.product.server.config.EmqxConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: maker_knz
 * @Date: 2021/6/1/001 10:05
 * @Version 1.0
 */

@Aspect
@Component
public class EmqxWebhookIdentityAspect {

    @Autowired
    private EmqxConfig emqxConfig;

    /**
     * 切面验证webhook接口是否可以访问
     * @param point
     * @return
     */
    @Around("@annotation(cn.makerknz.product.server.annotation.CheckEmqxWebhookIdentity)")
    public Object checkLogin(ProceedingJoinPoint point) {

        try {
            HttpServletRequest request = this.getHttpServletRequest();

            // 1.从header中获取username和passowrd
            String username = request.getHeader("webhook-username");
            String password = request.getHeader("webhook-password");

            // 2.验证账户的有效性
            String userPassword = emqxConfig.getUsers().get(username);
            if (!userPassword.equals(password)) {
                throw new SecurityException("没有权限使用WebHook");
            }

            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException("WebHook配置错误");
        }
    }

    /**
     * 从请求头中获取HttpServletRequest
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br><span class="line-number">32</span><br><span class="line-number">33</span><br><span class="line-number">34</span><br><span class="line-number">35</span><br><span class="line-number">36</span><br><span class="line-number">37</span><br><span class="line-number">38</span><br><span class="line-number">39</span><br><span class="line-number">40</span><br><span class="line-number">41</span><br><span class="line-number">42</span><br><span class="line-number">43</span><br><span class="line-number">44</span><br><span class="line-number">45</span><br><span class="line-number">46</span><br><span class="line-number">47</span><br><span class="line-number">48</span><br><span class="line-number">49</span><br><span class="line-number">50</span><br><span class="line-number">51</span><br><span class="line-number">52</span><br><span class="line-number">53</span><br><span class="line-number">54</span><br><span class="line-number">55</span><br><span class="line-number">56</span><br><span class="line-number">57</span><br><span class="line-number">58</span><br><span class="line-number">59</span><br><span class="line-number">60</span><br><span class="line-number">61</span><br><span class="line-number">62</span><br><span class="line-number">63</span><br><span class="line-number">64</span><br><span class="line-number">65</span><br></div></div><h3 id="webhook修改配置" tabindex="-1"><a class="header-anchor" href="#webhook修改配置" aria-hidden="true">#</a> Webhook修改配置</h3>
<div class="language-bash ext-sh line-numbers-mode"><pre v-pre class="language-bash"><code><span class="token function">docker</span> <span class="token function">ps</span>
<span class="token function">docker</span> <span class="token builtin class-name">exec</span> -it c46cb71330f1 <span class="token function">bash</span>
<span class="token builtin class-name">cd</span> /opt/emqx/etc/plugins
<span class="token function">vi</span> <span class="token function">vi</span> emqx_web_hook.conf
<span class="token builtin class-name">exit</span>
<span class="token function">docker</span> restart c46cb71330f1
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br></div></div><p>修改配置文件为</p>
<div class="language-properties ext-properties line-numbers-mode"><pre v-pre class="language-properties"><code><span class="token comment">##====================================================================</span>
<span class="token comment">## WebHook</span>
<span class="token comment">##====================================================================</span>

<span class="token comment">## Webhook URL</span>
<span class="token comment">##</span>
<span class="token comment">## Value: String</span>
<span class="token attr-name">web.hook.url</span> <span class="token punctuation">=</span> <span class="token attr-value">http://192.168.31.97:9200/emqx/webhook</span>

<span class="token comment">## HTTP Headers</span>
<span class="token comment">##</span>
<span class="token comment">## Example:</span>
<span class="token comment">## 1. web.hook.headers.content-type = application/json</span>
<span class="token comment">## 2. web.hook.headers.accept =</span>
<span class="token comment">##</span>
<span class="token comment">## Value: String</span>
<span class="token attr-name">web.hook.headers.content-type</span> <span class="token punctuation">=</span> <span class="token attr-value">application/json</span>

<span class="token comment">## 配置接口访问权限，不使用CA证书</span>
<span class="token attr-name">web.hook.headers.webhook-username</span> <span class="token punctuation">=</span> <span class="token attr-value">makerknz</span>
<span class="token attr-name">web.hook.headers.webhook-password</span> <span class="token punctuation">=</span> <span class="token attr-value">123456</span>

<span class="token comment">## The encoding format of the payload field in the HTTP body</span>
<span class="token comment">## The payload field only appears in the on_message_publish and on_message_delivered actions</span>
<span class="token comment">##</span>
<span class="token comment">## Value: plain | base64 | base62</span>
<span class="token attr-name">web.hook.body.encoding_of_payload_field</span> <span class="token punctuation">=</span> <span class="token attr-value">plain</span>

<span class="token comment">##-----------------------------使用https请求---------------------------------------</span>
<span class="token comment">## PEM format file of CA's</span>
<span class="token comment">##</span>
<span class="token comment">## Value: File</span>
<span class="token comment">## web.hook.ssl.cacertfile  = &lt;PEM format file of CA's></span>

<span class="token comment">## Certificate file to use, PEM format assumed</span>
<span class="token comment">##</span>
<span class="token comment">## Value: File</span>
<span class="token comment">## web.hook.ssl.certfile = &lt;Certificate file to use></span>

<span class="token comment">## Private key file to use, PEM format assumed</span>
<span class="token comment">##</span>
<span class="token comment">## Value: File</span>
<span class="token comment">## web.hook.ssl.keyfile = &lt;Private key file to use></span>

<span class="token comment">## Turn on peer certificate verification</span>
<span class="token comment">##</span>
<span class="token comment">## Value: true | false</span>
<span class="token comment">## web.hook.ssl.verify = false</span>

<span class="token comment">## If not specified, the server's names returned in server's certificate is validated against</span>
<span class="token comment">## what's provided `web.hook.url` config's host part.</span>
<span class="token comment">## Setting to 'disable' will make EMQ X ignore unmatched server names.</span>
<span class="token comment">## If set with a host name, the server's names returned in server's certificate is validated</span>
<span class="token comment">## against this value.</span>
<span class="token comment">##</span>
<span class="token comment">## Value: String | disable</span>
<span class="token comment">## web.hook.ssl.server_name_indication = disable</span>

<span class="token comment">## Connection process pool size</span>
<span class="token comment">##</span>
<span class="token comment">## Value: Number</span>
<span class="token comment">## HTTP 连接进程池大小。</span>
<span class="token attr-name">web.hook.pool_size</span> <span class="token punctuation">=</span> <span class="token attr-value">32</span>

<span class="token comment">## Whether to enable HTTP Pipelining</span>
<span class="token comment">##</span>
<span class="token comment">## See: https://en.wikipedia.org/wiki/HTTP_pipelining</span>
<span class="token attr-name">web.hook.enable_pipelining</span> <span class="token punctuation">=</span> <span class="token attr-value">true</span>

<span class="token comment">##--------------------------------------------------------------------</span>
<span class="token comment">## Hook Rules</span>
<span class="token comment">## These configuration items represent a list of events should be forwarded</span>
<span class="token comment">##</span>
<span class="token comment">## Format:</span>
<span class="token comment">##   web.hook.rule.&lt;HookName>.&lt;No> = &lt;Spec></span>
<span class="token comment">## 有客户端连接时触发</span>
<span class="token attr-name">web.hook.rule.client.connect.1</span>       <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_connect"}</span>
<span class="token comment">## EMQ X下发连接应答</span>
<span class="token attr-name">web.hook.rule.client.connack.1</span>       <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_connack"}</span>
<span class="token comment">## 客户端成功接入</span>
<span class="token attr-name">web.hook.rule.client.connected.1</span>     <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_connected"}</span>
<span class="token comment">## 客户端断开连接</span>
<span class="token attr-name">web.hook.rule.client.disconnected.1</span>  <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_disconnected"}</span>
<span class="token comment">## 客户端订阅事件</span>
<span class="token attr-name">web.hook.rule.client.subscribe.1</span>     <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_subscribe"}</span>
<span class="token comment">## 客户端取消订阅事件</span>
<span class="token attr-name">web.hook.rule.client.unsubscribe.1</span>   <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_client_unsubscribe"}</span>
<span class="token comment">## EMQ X确认订阅事件</span>
<span class="token attr-name">web.hook.rule.session.subscribed.1</span>   <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_session_subscribed"}</span>
<span class="token comment">## EMQ X确认取消订阅事件</span>
<span class="token attr-name">web.hook.rule.session.unsubscribed.1</span> <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_session_unsubscribed"}</span>
<span class="token comment">## 会话终止</span>
<span class="token attr-name">web.hook.rule.session.terminated.1</span>   <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_session_terminated"}</span>
<span class="token comment">## 发布消息</span>
<span class="token attr-name">web.hook.rule.message.publish.1</span>      <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_message_publish"}</span>
<span class="token comment">## 消息投递成功</span>
<span class="token attr-name">web.hook.rule.message.delivered.1</span>    <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_message_delivered"}</span>
<span class="token comment">## 消息已应答</span>
<span class="token attr-name">web.hook.rule.message.acked.1</span>        <span class="token punctuation">=</span> <span class="token attr-value">{"action": "on_message_acked"}</span>

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br><span class="line-number">17</span><br><span class="line-number">18</span><br><span class="line-number">19</span><br><span class="line-number">20</span><br><span class="line-number">21</span><br><span class="line-number">22</span><br><span class="line-number">23</span><br><span class="line-number">24</span><br><span class="line-number">25</span><br><span class="line-number">26</span><br><span class="line-number">27</span><br><span class="line-number">28</span><br><span class="line-number">29</span><br><span class="line-number">30</span><br><span class="line-number">31</span><br><span class="line-number">32</span><br><span class="line-number">33</span><br><span class="line-number">34</span><br><span class="line-number">35</span><br><span class="line-number">36</span><br><span class="line-number">37</span><br><span class="line-number">38</span><br><span class="line-number">39</span><br><span class="line-number">40</span><br><span class="line-number">41</span><br><span class="line-number">42</span><br><span class="line-number">43</span><br><span class="line-number">44</span><br><span class="line-number">45</span><br><span class="line-number">46</span><br><span class="line-number">47</span><br><span class="line-number">48</span><br><span class="line-number">49</span><br><span class="line-number">50</span><br><span class="line-number">51</span><br><span class="line-number">52</span><br><span class="line-number">53</span><br><span class="line-number">54</span><br><span class="line-number">55</span><br><span class="line-number">56</span><br><span class="line-number">57</span><br><span class="line-number">58</span><br><span class="line-number">59</span><br><span class="line-number">60</span><br><span class="line-number">61</span><br><span class="line-number">62</span><br><span class="line-number">63</span><br><span class="line-number">64</span><br><span class="line-number">65</span><br><span class="line-number">66</span><br><span class="line-number">67</span><br><span class="line-number">68</span><br><span class="line-number">69</span><br><span class="line-number">70</span><br><span class="line-number">71</span><br><span class="line-number">72</span><br><span class="line-number">73</span><br><span class="line-number">74</span><br><span class="line-number">75</span><br><span class="line-number">76</span><br><span class="line-number">77</span><br><span class="line-number">78</span><br><span class="line-number">79</span><br><span class="line-number">80</span><br><span class="line-number">81</span><br><span class="line-number">82</span><br><span class="line-number">83</span><br><span class="line-number">84</span><br><span class="line-number">85</span><br><span class="line-number">86</span><br><span class="line-number">87</span><br><span class="line-number">88</span><br><span class="line-number">89</span><br><span class="line-number">90</span><br><span class="line-number">91</span><br><span class="line-number">92</span><br><span class="line-number">93</span><br><span class="line-number">94</span><br><span class="line-number">95</span><br><span class="line-number">96</span><br><span class="line-number">97</span><br><span class="line-number">98</span><br><span class="line-number">99</span><br><span class="line-number">100</span><br></div></div><h2 id="测试" tabindex="-1"><a class="header-anchor" href="#测试" aria-hidden="true">#</a> 测试</h2>
<h3 id="接口测试" tabindex="-1"><a class="header-anchor" href="#接口测试" aria-hidden="true">#</a> 接口测试</h3>
<h3 id="权限测试" tabindex="-1"><a class="header-anchor" href="#权限测试" aria-hidden="true">#</a> 权限测试</h3>
<h2 id="总结" tabindex="-1"><a class="header-anchor" href="#总结" aria-hidden="true">#</a> 总结</h2>
<p>这里使用WebFlux可以很好的起到消息中间件的作用，这也是为什么不使用kafka作为消息件的原因。如果使用消息订阅的方式，后面对MQTT消息处理是一件复杂的过程。</p>
</template>
