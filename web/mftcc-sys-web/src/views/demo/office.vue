<template>
  <div id="iframeEditor"></div>
</template>
<script>
import DocsAPI from "#/mftccOffice.js";
export default {
  data() {
    return {
      config: {
        url: "http://192.168.110.161",
        document: {
          fileType: "docx",
          key: "kmuqp4qnw0h148psc268",
          permissions: {
            comment: true,
            download: true,
            edit: true,
            fillForms: true,
            modifyContentControl: true,
            modifyFilter: true,
            review: true,
          },
          title: "模板渲染 DEMO.docx",
          url:
            // "http://192.168.110.12:38080/app_data/5431651141e74510973c8f7916bfbed8/20210128202415.docx",
            "http://127.0.0.1:7038/mftcc-sys-web/static/template/new.docx",
        },
        documentType: "text",
        editorConfig: {
          callbackUrl: "http://192.168.110.12:38080/doc/docmanage/track/21",
          customization: {
            autosave: false,
            chat: false,
            comments: false,
            forcesave: true,
            goback: { url: "http://192.168.110.12:38080/" },
            help: false,
          },
          lang: "zh",
          mode: "edit",
          user: { id: "admin", name: "admin" },
        },
        height: "100%",
        lang: "zh",
        mode: "edit",
        type: "desktop",
        width: "100%",
      },
    };
  },
  mounted() {
    let templateId = this.$route.query.templateId;
    DocsAPI.DocEditor("iframeEditor", this.config);
  },
};
</script>
