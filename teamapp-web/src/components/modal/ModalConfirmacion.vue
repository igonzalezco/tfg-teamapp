<template>
    <div :id="id" class="modal fade" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <h6 class="font-weight-semibold" v-html="contentModal"></h6>
                </div>
                <div class="modal-footer">
                    <el-button data-dismiss="modal"
                      :disabled="deshabilitarBotonesModal"
                    >{{ $t('lang.modal.cancelar')  }}
                    </el-button>
                    <el-button :disabled="deshabilitarBotonesModal" data-dismiss="modal" @click="doAction">{{ textButtonDoAction }}</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: "ModalConfirmacion",
    data() {
        return {
            modal: false
        }
    },   
    props: {
        id: String,
        contentModal: String,
        codigo: String,
        textButtonDoAction: String,
        onclick: Function,
        deshabilitarBotonesModal: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        doAction: function() {
            this.onClick();
        }
    }
}
</script>