<template>
  <div class="container">
    <helper-a @write-book="writeBook" @get-all="getAll" ref="helperA" id="helper-a"></helper-a>
    <helper-b @update-count="updateCount" @open-edit="openEdit" @card-click="cardClick" ref="helperB"
      id="helper-b"></helper-b>
    <div id="helper-main">
      <helper-c @get-all="getAll" @flus-label-menu="flusLabelMenu" @get-by-id="getById" @open-drawer="openDrawer"
        ref="helperC" id="helper-c"></helper-c>
      <helper-d @flush-menu="flushMenu" @save-success="saveSuccess" ref="helperD" id="helper-d"></helper-d>
    </div>
  </div>
</template>

<script>
import HelperA from './helper/helper-a.vue'
import HelperB from './helper/helper-b.vue'
import HelperC from './helper/helper-c.vue'
import HelperD from './helper/helper-d.vue'
export default {
  components: {
    HelperA, HelperB, HelperC, HelperD
  },
  data() {
    return {
      drawerD: false,
    }
  },
  methods: {
    // 保存标签后刷新菜单
    flusLabelMenu() {
      this.$refs.helperA.flusLabelMenu();
    },
    //修改统计数量
    updateCount() {
      this.$refs.helperA.getCount();
    },
    //刷新菜单状态
    flushMenu() {
      this.$refs.helperA.flushMenu();
    },

    //写笔记
    writeBook() {
      this.$refs.helperD.writeBook();
    },
    //编辑
    openEdit(item) {
      this.$refs.helperD.openEdit(item);
    },
    //点击菜单
    getAll() {
      let menu = window.sessionStorage.getItem('menu');
      if (menu === 'note') {
        this.$refs.helperB.getLsit();
      } else if (menu === 'sign') {
        this.$refs.helperB.getSign();
      } else if (menu === 'dele') {
        console.log(menu);
      } else if (menu === 'label') {
        console.log(menu);
      } else {
        // console.log(menu);
        this.$refs.helperB.labelSearch();
      }
    },
    //根据id获取helper
    getById(id) {
      this.$http.get(
        '/helper/' + id
      ).then(res => {
        if (res.data.data === null) { return false; }
        this.cardClick(res.data.data);
        return;
      }).catch(err => {
        console.log(err);
        return;
      })
    },
    // 打开抽屉
    openDrawer(item) {
      this.$refs.helperD.openDrawer(true);
    },
    //卡片点击
    cardClick(item) {
      window.localStorage.setItem('helper', item.id);
      this.$refs.helperD.cardClick(item);
      this.$refs.helperC.cardClick(item);
    },
    //保存成功
    saveSuccess() {
      this.$refs.helperB.getLsit();
    }
  },
  created() {
    const id = window.localStorage.getItem('helper');
    if (id !== null) {
      this.getById(id);
    }
    return;
  }
}
</script>
<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
  overflow: hidden;
}

#helper-a {
  width: 200px;
}

#helper-b {
  width: 400px;
}

#helper-main {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  #helper-c {
    width: 100%;
    height: 60px;
  }

  #helper-d {
    width: 100%;
    height: 100%;
  }
}
</style>
