<template>
    <div id="helper-b">
        <div id="col-a">
            <el-input @keyup.enter.native="searchText()" v-model="page.quiry.notebook" placeholder="请输入内容">
                <i style="cursor: pointer;" @click="searchText()" slot="suffix" class="el-input__icon el-icon-search"></i>
            </el-input>
        </div>
        <div id="col-b">
            <el-card :style="item.sign === true ? 'border: 2px solid #1e80ff' : ''" v-for="item in dataList" :key="item.id"
                shadow="hover">
                <div class="card_inner" @click="cardClick(item)">
                    <div class="top">
                        <p class="card-time">{{ item.updateTime }}</p>
                        <i :style="item.sign === true ? '' : 'display:none;'" class="el-icon-collection-tag"></i>
                    </div>
                    <div class="body">
                        <p class="body1">{{ item.notebook.substring(0, item.notebook.indexOf('\n')).replace('#', '') }}</p>
                        <p class="body2">{{ item.notebook }}</p>
                    </div>
                    <div class="beta">
                        <div>
                            <el-tag v-for="sign in item.labelList" :key="sign.id">{{ sign.label }}</el-tag>
                        </div>
                        <el-dropdown trigger="click">
                            <el-button icon="el-icon-more" @click.stop="cardStatus"></el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item><span class="dropdown-span" @click="sign(item)">{{
                                    item.sign === true ? '取消星标' : '星标' }}</span></el-dropdown-item>
                                <el-dropdown-item><span class="dropdown-span"
                                        @click="edit(item)">编辑</span></el-dropdown-item>
                                <el-dropdown-item><span class="dropdown-span"
                                        @click="dele(item)">删除</span></el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </div>
            </el-card>
        </div>

    </div>
</template>

<script>
export default {
    data: function() {
        return {
            search: '',
            // 列表数据
            dataList: [],
            //分页
            page: {
                current: '',
                pageSize: '',
                total: 0,
                signNum: 0,
                quiry: {
                    notebook: '',
                    sign: ''
                }
            }
        }
    },
    props: {

    },
    methods: {
        // 图标点击分页查询
        labelSearch() {
            const menu = window.sessionStorage.getItem('menu');
            console.log(menu);
            this.$http.get('/label/getPageByHelperId', {
                params: {
                    labelId: menu,
                    current: this.page.current,
                    pageSize: this.page.pageSize,
                    notebook: this.page.quiry.notebook,
                    sign: this.page.quiry.sign
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.error('获取笔记出错啦,请稍后再试!'); }
                console.log(res.data);
                this.page.total = res.data.data.total;
                this.page.current = res.data.data.current;
                this.page.pageSize = res.data.data.size;
                this.dataList = res.data.data.records;
            }).catch(err => {
                console.log(err);
                return this.$message.error('获取笔记出错啦,请稍后再试!');
            })
        },
        //搜索框
        searchText() {
            const menu = window.sessionStorage.getItem('menu');
            if (menu === 'note') {
                this.getLsit();
            } else if (menu === 'sign') {
                this.getSign();
            } else if (menu === 'dele') {
                console.log(menu);
            } else if (menu === 'label') {
                console.log(menu);
            } else {
                this.labelSearch();
            }
        },
        //获取星标笔记
        getSign() {
            this.$http.get('/helper/page', {
                params: {
                    current: this.page.current,
                    pageSize: this.page.pageSize,
                    notebook: this.page.quiry.notebook,
                    sign: true
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.error('获取笔记出错啦,请稍后再试!'); }
                this.page.signNum = res.data.data.total;
                this.page.current = res.data.data.current;
                this.page.pageSize = res.data.data.size;
                this.dataList = res.data.data.records;
            }).catch(err => {
                console.log(err);
                return this.$message.error('获取笔记出错啦,请稍后再试!');
            })
        },
        //修改星标点击
        sign(item) {
            this.$http.put('/helper', {
                "id": item.id,
                "sign": !item.sign,
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.error('修改出错啦,请稍后再试!') }
                const menu = window.sessionStorage.getItem('menu');
                console.log(11);
                this.$emit('update-count');
                if (menu === 'note') {
                    this.getLsit();
                } else if (menu === 'sign') {
                    this.getSign();
                }
                return;
            }).catch(err => {
                return this.$message.error('修改出错啦,请稍后再试!')
            });
        },
        edit(item) {
            this.$emit("open-edit", item);
        },
        dele(item) {
            this.$http.delete('/helper', {
                params: {
                    ids: item.id
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.success('删除出错啦,请稍后再试!') }
                this.$message.success('删除成功');
                this.getLsit();
                return;
            }).catch(err => {
                return this.$message.success('删除出错啦,请稍后再试!');
            });
            return;
        },
        cardClick(item) {
            //点击卡片时想办法让当前卡片立体起来
            // document.getElementById(item.id).shadow = 'always';
            this.$emit('card-click', item);
        },
        //卡片状态
        cardStatus() {

        },
        //获取数据
        getLsit() {
            this.$http.get('/helper/page', {
                params: {
                    current: this.page.current,
                    pageSize: this.page.pageSize,
                    notebook: this.page.quiry.notebook,
                    sign: this.page.quiry.sign
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.error('获取笔记出错啦,请稍后再试!'); }
                this.page.total = res.data.data.total;
                this.page.current = res.data.data.current;
                this.page.pageSize = res.data.data.size;
                this.dataList = res.data.data.records;

            }).catch(err => {
                console.log(err);
                return this.$message.error('获取笔记出错啦,请稍后再试!');
            })
        }
        //下拉触底
    },
    created() {
        const menu = window.sessionStorage.getItem('menu');
        if (menu === 'note') {
            this.getLsit();
        } else if (menu === 'sign') {
            this.getSign();
        } else if (menu === 'dele') {
            console.log(menu);
        }
    }
}
</script>
<style lang="less" scoped>
.el-dropdown-menu__item {
    padding: 0;
}

.dropdown-span {
    padding: 1px 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    width: auto;
}

.top {
    display: flex;
    justify-content: space-between;

    i {
        color: #1e80ff;
    }
}

#helper-b {

    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-left: 1px solid #e4e6eb;
    border-right: 1px solid #e4e6eb;
    box-sizing: border-box;

    #col-a {
        width: 100%;

        .el-input {
            width: 65%;
            margin: 21px 5px 20px 20px;


            ::v-deep .el-input__inner {
                padding: 0 15px;
                height: 30px;
                border-radius: 1px;
                color: #8a919f;
                background-color: #f7f8fa;
            }

            ::v-deep .el-input__suffix {
                display: flex;
                justify-content: center;
                align-items: center;
            }
        }
    }

    #col-b {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: auto;
        overflow-x: hidden;

    }

    .el-card {
        cursor: pointer;
        display: flex;
        flex-direction: column;
        width: 90%;
        margin-bottom: 20px;
        overflow: visible;

        .card_inner {
            padding: 20px;
        }

        ::v-deep .el-card__body {
            padding: 0 !important;
        }

        .card-time {
            font-size: small;
            color: #8a919f;
            margin: 0;
        }

        .body {
            margin: 10px 0;

            .body1 {
                margin: 0;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
                font-size: 16px;
                margin: 5px 0;
                color: #252933;
            }

            .body2 {
                margin: 0;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
                font-size: 12px;
                color: #8a919f;
            }
        }

        .beta {
            display: flex;
            width: 100%;
            justify-content: space-between;

            .el-tag {
                margin-right: 5px;
                height: 25px;
                line-height: 23px;
                font-size: 8px;
                border-radius: 2px
            }

            .el-button {
                width: 20px;
                height: 20px;
                padding: 0;
                border-radius: 0;
                border: none;
            }

        }
    }
}
</style>
