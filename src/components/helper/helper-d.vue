<template>
    <div class="">
        <el-container>
            <el-main>
                <Viewer class="viewerMain" :tabindex="2" :sanitize="23" :value="value" :plugins="plugins" :locale="zhHans">
                </Viewer>
            </el-main>
        </el-container>

        <!-- 编辑抽屉 -->
        <el-drawer size="95%" :with-header="false" :visible.sync="drawer" direction="ltr" :before-close="handleClose">
            <Editor class="editosDrawer" :value="value" :plugins="plugins" :locale="zhHans" @change="handleChange" :uploadImages="uploadImage"></Editor>
        </el-drawer>
        <!-- 新建抽屉 -->
        <el-drawer size="95%" :with-header="false" :visible.sync="drawerSave" direction="ltr" :before-close="handleSave">
            <Editor class="editosDrawer" :value="value" :plugins="plugins" :locale="zhHans" @change="handleChange" :uploadImages="uploadImage"></Editor>
        </el-drawer>
    </div>
</template>

<script>
import { Editor, Viewer } from '@bytemd/vue'
import gfm from '@bytemd/plugin-gfm'
import highlight from '@bytemd/plugin-highlight'
import breaks from '@bytemd/plugin-breaks'
import footnotes from '@bytemd/plugin-footnotes'
import frontmatter from '@bytemd/plugin-frontmatter'
import gemoji from '@bytemd/plugin-gemoji'
import mediumZoom from '@bytemd/plugin-medium-zoom'
import mermaid from '@bytemd/plugin-mermaid'
import mathssr from '@bytemd/plugin-math-ssr'
// import { getProcessor } from 'bytemd'
import zhHans from 'bytemd/locales/zh_Hans.json'
import gfmLocale from '@bytemd/plugin-gfm/locales/zh_Hans.json';
import mathLocale from '@bytemd/plugin-math/locales/zh_Hans.json';
import mermaidLocale from '@bytemd/plugin-mermaid/locales/zh_Hans.json';
import 'highlight.js/styles/vs.css'
// import 'juejin-markdown-themes/dist/juejin.min.css'  // 掘金风格的css文件
import '../../assets/css/channing-cyan.scss'  // channing-cyan风格的css文件，这个确实很好看
import 'bytemd/dist/index.css'  // 导入编辑器样式

const plugins = [
    // 将所有的扩展功能放入插件数组中，然后就可以生效了
    gfm({ locale: gfmLocale }),
    // highlightssr(),
    breaks(),
    highlight(),
    mermaid({ locale: mermaidLocale }),
    mathssr({ locale: mathLocale }),
    frontmatter(),
    footnotes(),
    gemoji(),
    mediumZoom()
]

export default {
    props: {
    },
    computed: {

    },
    components: { Editor, Viewer },
    data() {
        return {
            //当前选中笔记的数据
            noteData: '',
            drawer: false,
            drawerSave: false,//保存抽屉
            value: '',
            plugins: plugins,  // 插件
            zhHans, // 简体中文
        }
    },
    methods: {
        //写笔记
        writeBook() {
            this.noteData = '';
            this.value = '';
            this.drawerSave = true;
        },
        //编辑
        openEdit(item) {
            this.noteData = item;
            this.value = item.notebook;
            this.drawer = !this.drawer;
        },
        //卡片点击
        cardClick(item) {
            this.noteData = item;
            this.value = item.notebook;
        },
        //打开抽屉
        openDrawer(v) {
            this.drawer = v;
        },
        //保存
        handleSave() {
            this.$confirm('是否保存？', '确认信息', {
                distinguishCancelAndClose: true,
                confirmButtonText: '保存',
                cancelButtonText: '放弃'
            }).then(() => {
                if (this.value === '') {
                    this.$message.error('空内容不能保存哦!');
                    this.drawerSave = false;
                    return false;
                }
                this.$http.post('/helper', {
                    "notebook": this.value,
                    "sign": false
                }).then(res => {
                    if (res.data.code !== 0) { return this.$message.error('保存出错啦!') }
                    this.$emit("save-success");
                    this.$message.success({
                        showClose: true,
                        message: '保存成功',
                        type: 'success'
                    })
                    this.drawerSave = false;
                    window.sessionStorage.setItem('menu', 'note');
                    this.$emit("flush-menu");
                    return;
                }).catch(err => {
                    return this.$message.error('保存出错啦!')
                });

            }).catch(action => {
                if (action === 'cancel') {
                    //放弃保存并离开页面
                    this.$message.info('取消保存!')
                    this.drawerSave = false;
                    return;
                } else {
                    //停留在当前页面
                    return;
                }
            });
        },
        //修改
        handleClose() {
            this.$confirm('是否保存修改？', '确认信息', {
                distinguishCancelAndClose: true,
                confirmButtonText: '保存',
                cancelButtonText: '放弃修改'
            }).then(() => {
                this.$http.put('/helper', {
                    "id": this.noteData.id,
                    "notebook": this.value,
                }).then(res => {
                    if (res.data.code !== 0) { return this.$message.error('保存出错啦!') }
                    this.$emit("save-success");
                    this.$message.success('保存成功')
                    this.drawer = false;
                    return;
                }).catch(err => {
                    return this.$message.error('保存出错啦!')
                });
            }).catch(action => {
                if (action === 'cancel') {
                    //放弃保存并离开页面
                    this.$message.info('取消编辑')
                    this.drawer = false;
                    return;
                } else {
                    //停留在当前页面
                    return;
                }
            });
        },
        // 获取书写文档内容
        handleChange(v) {
            this.$emit("input", v)
            this.value = v
        },
        // 上传图片 点击触发上传图片事件，大家获取文件把图片上传服务器然后返回url既可
        async uploadImage(files) {
            // console.log('files', files)
            return [
                {
                    title: files.map((i) => i.name),
                    url: 'http'
                }
            ]
        }
    },
    created() {
    }
}
</script>
<style lang="less" scoped>
.el-main {
    overflow: visible;
}

.el-container {
    height: 100%;
    display: flex;
    justify-content: center;
    overflow: auto;

    .el-main {
        height: max-content;
        padding: 0 0 60px 0;
        max-width: 800px;


        .viewerMain {
            padding: 20px;

        }
    }
}

.editos {
    width: 100%;
    height: 100%;
}

.el-drawer {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

.el-drawer__body {
    display: flex;
    flex: none;
    overflow: none;
    height: 100% !important;

    .editosDrawer {
        width: 100%;
        height: 100%;

        ::v-deep .bytemd {
            height: 100%;
        }
    }
}
</style>
