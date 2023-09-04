<template>
    <div id="helper-a">
        <div id="col-a">
            <img src="../../assets/images/JunTian-no.png" alt="">
        </div>
        <el-button @click="$emit('write-book')" id="addButton" type="primary">
            <i class="el-icon-edit"></i>
            <span id="writeNote">写笔记</span>
        </el-button>
        <div id="col-b">
            <el-menu :default-active="activeMenu" :collapse-transition="false" :collapse="isCollapse" text-color="#505767"
                active-text-color="#1e80ff" @select='selectmenu'>
                <el-menu-item index="note">
                    <div class="menu-item-title">
                        <i class="menuIcon-b" :class="'el-icon-s-order'"></i>
                        <span class="span1">全部笔记</span>
                        <span class="span2">{{ total }}</span>
                    </div>
                </el-menu-item>
                <el-menu-item index="sign">
                    <div class="menu-item-title">
                        <i class="menuIcon-b" :class="'el-icon-star-on'"></i>
                        <span class="span1">星标笔记</span>
                        <span class="span2">{{ siginNum }}</span>
                    </div>
                </el-menu-item>
                <!-- <el-menu-item index="dele">
                    <div class="menu-item-title">
                        <i class="menuIcon-b" :class="'el-icon-delete-solid'"></i>
                        <span class="span1">最近删除</span>
                        <span class="span2">{{ deleNum }}</span>
                    </div>
                </el-menu-item> -->
                <el-menu-item index="label">
                    <div class="menu-item-title" @click="handleOpen">
                        <i class="menuIcon-b" :class="'el-icon-s-flag'"></i>
                        <span class="span1">我的标签</span>
                        <span class="span2">{{ labelNum }}</span>
                    </div>
                </el-menu-item>
                <el-menu-item :style="isShowLabel === true ? '' : 'display: none;'" v-for="item in labelData" :key="item.id"
                    :index="item.id">
                    <div class="menu-item-title">
                        <span class="span1">{{ item.label }}</span>
                    </div>
                </el-menu-item>
            </el-menu>
        </div>
        <div id="col-c">
            <div class="col-c-bottom">
                <span class="BeiAn">©2023 JT-Helper</span>
                <span class="BeiAn" style="cursor:pointer;" @click="toWebsite">黔ICP备2023003770号-1</span>
                <div>
                    <img style="width: 10px; height: 10px;" src="../../assets/images/GongAn.png">
                    <span class="BeiAn" style="cursor:pointer;" @click="toGongAn">贵公网安备52030302000848号</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data: function() {
        return {
            isShowLabel: false,     //是否展开标签栏
            labelData: [],          //标签数组
            activeMenu: 'note',     // 默认激活
            total: 0,        // 菜单数组
            siginNum: 0,
            deleNum: 0,
            labelNum: 0,
            screenWidth: 0,            // 屏幕宽度
            isCollapse: false          //是否影藏导航栏
        }
    },
    props: {

    },
    methods: {
        // 保存标签后刷新菜单
        flusLabelMenu() {
            this.$http.get('/label'
            ).then(res => {
                if (res.data.code !== 0) { return false; }
                this.labelData = res.data.data;
                return true;
            }).catch(err => {
                return false;
            })
        },
        // 打开标签栏触发
        handleOpen() {
            if (!this.isShowLabel) {
                this.isShowLabel = true;
                this.$http.get('/label'
                ).then(res => {
                    if (res.data.code !== 0) { return this.$message.error('获取标签出错,请稍后再试!'); }
                    this.labelData = res.data.data;
                    return true;
                }).catch(err => {
                    this.$message.error('获取标签出错,请稍后再试!');
                    return false;
                })
                return;
            } else {
                this.isShowLabel = false;
            }
        },
        //查询笔记数量和星标笔记数量,标签数量
        getCount() {
            this.$http.get('/helper/page').then(res => {
                if (res.data.code !== 0) { return this.$message.error('获取笔记出错啦!'); }
                this.total = res.data.data.total;
            }).catch(err => {
                console.log(err);
                return this.$message.error('获取笔记出错啦!');
            })
            this.$http.get('/helper/page', {
                params: {
                    sign: true
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.error('获取笔记出错啦!'); }
                this.siginNum = res.data.data.total;
            }).catch(err => {
                console.log(err);
                return this.$message.error('获取笔记出错啦!');
            })
        },
        //选中菜单触发事件
        selectmenu(index, indexPath) {
            this.activeMenu = index;
            window.sessionStorage.setItem('menu', this.activeMenu);
            this.$emit('get-all');
        },
        //刷新菜单状态
        flushMenu() {
            const menu = window.sessionStorage.getItem('menu');
            this.activeMenu = menu;
        },
        // 转跳管局官网
        toWebsite() {
            window.open(`https://beian.miit.gov.cn`);
        },
        // 转跳公安官网
        toGongAn() {
            window.open(`http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=52030302000848`);
        }
    },
    created() {
        this.activeMenu = window.sessionStorage.getItem('menu');
        this.getCount();
    },
    mounted() {
        //获取屏幕尺寸
        this.screenWidth = document.body.clientWidth
        window.onresize = () => {
            //屏幕尺寸变化
            return (() => {
                this.screenWidth = document.body.clientWidth
            })()
        }
    },
    watch: {
        // 屏幕尺寸变化则触发
        screenWidth: function(n, o) {
            if (n < 1200) {
                this.isCollapse = true;
                document.getElementById('writeNote').style.display = 'none';
                document.getElementById('addButton').style.width = '20%';
                document.getElementById('helper-a').style.width = '100px';
                document.getElementById('col-b').style.width = '50%';

            }
            if (n > 1200) {
                this.isCollapse = false;
                document.getElementById('writeNote').style.display = '';
                document.getElementById('addButton').style.width = '80%';
                document.getElementById('helper-a').style.width = '200px';
                document.getElementById('col-b').style.width = '100%';

            }
        }
    }
}
</script>
<style lang="less" scoped>
#helper-a {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-left: 1px solid #e4e6eb;
    border-right: 1px solid #e4e6eb;
    box-sizing: border-box;

    .el-button {
        width: 80%;
        margin: 20px 0;
        display: flex;
        justify-content: center;
    }

    #col-a img {
        width: 100%;
        height: 50px;
        margin-left: 0;
    }

    .el-menu--collapse {
        width: auto;
    }

    #col-b {
        width: 100%;
        height: 500px;

        .el-menu {
            // height: 100%;
            // width: 80%;
            // display: flex;
            // flex-direction: column;
            // align-items: center;
            border-right: none;
            // overflow-y: visible;
            // overflow-x: hidden;
        }

        .el-menu-item {
            padding-left: 10px !important;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
            height: 46px;
            width: 100%;

            .menu-item-title {
                flex-grow: 1;
                height: 46px;
                display: flex;
                flex-direction: row;
                align-items: center;
                justify-content: flex-start;

                i {
                    font-size: 16px;
                }

                .span1 {
                    line-height: 46px;
                    font-size: 12px;
                }

                .span2 {
                    line-height: 46px;
                    font-size: 12px;
                    margin-left: 20px;
                }
            }
        }
    }
}

#col-c {
    margin-top: auto;
    width: 100%;
    margin-bottom: 2.6666666667rem;
    text-align: center;

    .col-c-bottom {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .BeiAn {
            font-size: 12px;
            color: #8a919f;
            margin-top: 6px;
        }

    }
}




/*chrome--------------------------------------------start*/
::-webkit-scrollbar {
    width: 0px;
    height: 8px;
}

/* Track */
::-webkit-scrollbar-track {
    background: rgb(255, 255, 255);
    border-radius: 8px;
}

/* Handle */
::-webkit-scrollbar-thumb {
    background: #e4e6eb;
    border-radius: 8px;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
    background: #e4e6eb;
}

.el-menu::-webkit-scrollbar,
.el-table__body-wrapper::-webkit-scrollbar {
    display: none;
}

.el-menu:hover::-webkit-scrollbar,
.el-table__body-wrapper:hover::-webkit-scrollbar {
    display: block;
}

/*chrome--------------------------------------------end*/
/*IE--------------------------------------------start*/
* {
    /*三角箭头的颜色*/
    scrollbar-arrow-color: #c0c4cc;
    /*滚动条滑块按钮的颜色*/
    scrollbar-face-color: #A2A2A3;
    /*滚动条整体颜色*/
    scrollbar-highlight-color: #A2A2A3;
    /*滚动条阴影*/
    scrollbar-shadow-color: #A2A2A3;
    /*滚动条轨道颜色*/
    scrollbar-track-color: #f4f4f5;
    /*滚动条3d亮色阴影边框的外观颜色——左边和上边的阴影色*/
    scrollbar-3dlight-color: #A2A2A3;
    /*滚动条3d暗色阴影边框的外观颜色——右边和下边的阴影色*/
    scrollbar-darkshadow-color: #A2A2A3;
    /*滚动条基准颜色*/
    scrollbar-base-color: #f4f4f5;
}

.el-table__body-wrapper,
.el-menu {
    /*IE下隐藏*/
    -ms-overflow-style: none;
}

.el-table__body-wrapper:hover,
.el-menu:hover {
    /*IE下显示*/
    -ms-overflow-style: auto;
}

/*IE--------------------------------------------end*/
</style>
