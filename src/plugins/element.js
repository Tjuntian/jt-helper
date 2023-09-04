import Vue from 'vue'
import {
    Button,
    Container,
    Header,
    Main,
    Footer,
    Menu,
    MenuItem,
    Input,
    Card,
    Tag,
    InfiniteScroll,
    Avatar,
    Drawer,
    MessageBox,
    Message,
    Autocomplete,
    Form,
    FormItem,
    Dropdown,
    DropdownMenu,
    DropdownItem,
    Submenu,
    MenuItemGroup
} from 'element-ui'


Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$message = Message

Vue.use(Button)
Vue.use(Container)
Vue.use(Header)
Vue.use(Main)
Vue.use(Footer)
Vue.use(Menu)
Vue.use(MenuItem)
Vue.use(Input)
Vue.use(Card)
Vue.use(Tag)
Vue.use(InfiniteScroll)
Vue.use(Avatar)
Vue.use(Drawer)
Vue.use(Autocomplete)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Dropdown)
Vue.use(DropdownMenu)
Vue.use(DropdownItem)
Vue.use(Submenu)
Vue.use(MenuItemGroup)
