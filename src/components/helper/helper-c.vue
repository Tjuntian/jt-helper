<template>
    <div>
        <el-container>
            <el-header>
                <div class="header-left">
                    <span>{{ '2023年5月31日 14:06 已保存' }}</span>
                    <el-autocomplete value-key="label" value="label" :minlength="1" :maxlength="30" class="header-input"
                        v-model="sign" :fetch-suggestions="querySearchAsync" placeholder="添加标签..."
                        @select="handleSelect"></el-autocomplete>
                    <el-tag closable @close="handleClose(tag)" :key="tag.id" v-for="tag in dynamicTags">{{ tag.label
                    }}</el-tag>
                </div>
                <div class="header-right">
                    <el-button @click="$emit('open-drawer')" size="medium">编辑</el-button>
                    <el-dropdown @command="handleCommand" trigger="click">
                        <el-avatar style="cursor: pointer;" size="medium"
                            :src="require('../../assets/images/logo.png')"></el-avatar>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="exitLogin">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
        </el-container>
    </div>
</template>
<script>
export default {
    data() {
        return {
            // 该数据所拥有的标签
            dynamicTags: [],
            // 标签建议值
            restaurants: [],
            //标签输入值
            inputValue: [],
            // 抽屉是否打开
            drawer: false,
            //标签值
            sign: ''
        }
    },
    methods: {
        // 卡片关闭
        handleClose(tag) {
            const labelId = tag.id;
            const helperId = window.localStorage.getItem('helper');
            this.$http.delete('/label/delLabelByHelperIdAndLabelId', {
                params: {
                    helperId: helperId,
                    labelId: labelId,
                }
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.info(res.data.msg) }
                this.$emit('get-by-id', helperId);
                this.$emit('flus-label-menu');
                this.$emit('get-all');
                return true;
            }).catch(err => {
                console.log(err);
                return false;
            })

        },
        //卡片点击,标签赋值
        cardClick(item) {
            this.dynamicTags = item.labelList;
        },
        // 移除cookie
        removeCookie(name) {
            document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;';
        },
        // 退出登录
        handleCommand(command) {
            if (command === 'exitLogin') {
                this.removeCookie('token');
                this.$router.push('/login');
                return this.$message.success('退出登录');
            }

        },
        async loadAll() {
            const { data: res } = await this.$http.get('/label');
            this.restaurants = res.data;
        },
        createStateFilter(queryString) {
            return (state) => {
                return (state.label.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        querySearchAsync(queryString, cb) {
            this.loadAll();
            var restaurants = this.restaurants;
            var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;

            results.push({
                "id": undefined,
                "createTime": undefined,
                "updateTime": undefined,
                "label": queryString
            })
            cb(results)
        },
        // 选中时触发
        handleSelect(item) {
            this.sign = '';
            const helperId = window.localStorage.getItem('helper');
            this.$http.post('/label/saveLabelByHelperId?helperId=' + helperId, {
                "id": item.id,
                "createTime": item.createTime,
                "updateTime": item.updateTime,
                "label": item.label
            }).then(res => {
                if (res.data.code !== 0) { return this.$message.info(res.data.msg) }
                this.$emit('get-by-id', helperId);
                this.$emit('flus-label-menu');
                this.$emit('get-all');
                return true;
            }).catch(err => {
                console.log(err);
                return false;
            })
        }
    },
    mounted() {
        this.loadAll();
    }
}
</script>
<style lang="less" scoped>
.el-container {
    .el-header {
        border-bottom: 1px solid #e4e6eb;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;

        .header-left {
            display: flex;
            flex-direction: row;
            align-items: center;

            span {
                font-size: 12px;
                color: #8a919f;
                font-weight: 400;
                line-height: 22px;
                margin: 0;
            }

            .header-input {
                ::v-deep .el-input__inner {
                    background-color: #f2f3f5;
                    width: 120px;
                    height: 32px;
                    font-size: 14px;
                    border-radius: 1px;
                    margin-left: 30px;
                    color: #8a919f;
                }
            }

            .el-tag {
                height: 32px;
                font-size: 14px;
                line-height: 30px;
                border-radius: 1px;
                margin-left: 10px;
            }


        }

        .header-right {
            display: flex;
            justify-content: center;
            flex-direction: row;
            align-items: center;

            .el-button {
                background: #f3f9ff;
                color: #1e80ff;
                margin-right: 20px;
                height: 32px;
                font-size: 14px;
                border-radius: 1px;
                width: 60px;
                display: flex;
                flex-direction: row;
                justify-content: center;
                align-items: center;
            }
        }
    }
}
</style>
