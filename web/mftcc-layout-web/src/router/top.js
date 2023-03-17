let top = [
  {
    //登录页
    path: "/login",
    component: () => import("@/views/login/login")
  },
  { path: "/", redirect: "/login" },
  { path: "/" + $productName, redirect: "/login" },
  {
    //文档编辑
    path: "/doc",
    component: () => import("@/views/doc/office")
  },
  {
    //PC审批独立页面
    path: "/appPagePc",
    component: () => import("@/views/toDoflowable/appPage")
  },
  {
    //移动端审批独立页面
    path: "/appPageMobile",
    component: () => import("@/views/toDoflowable/mobile/appMobilePage")
  },
  {
    //审批独立页面-自动判断移动端 pc端
    path: "/appPage",
    component: () => import("@/views/toDoflowable/standalone")
  }
];
export default top;
