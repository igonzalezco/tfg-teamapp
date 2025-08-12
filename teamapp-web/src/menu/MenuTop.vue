<template>
    <div>
        <div class="navbar navbar-expand-md navbar-ligth fixed-top">
            <div class="navbar-brand">
                <router-link class="d-inline-block" :to="{ name: 'initContent'}">
                    <img class="img-menu" src="@/assets/images/logo-full.png" alt/>
                </router-link>
            </div>

           <div class="navbar-nav ms-auto dropdown-user">
                <div class="flex flex-wrap items-center">
                    <el-dropdown>
                       <a 
                        href="#"
                        class="navbar-nav-link d-flex align-items-center"
                        >
                            <img src="@/assets/images/avatar.png" class="rounded-circle mr-2" height="90" alt />
                            <span v-if="user != null" > {{user.name}} </span>
                        </a>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>
                                    <router-link :to="{ name: 'initContent' }">
                                        {{ $t('lang.mi-perfil') }}
                                    </router-link>
                                </el-dropdown-item>
                                <el-dropdown-item divided>
                                    <a
                                        href="#"
                                        data-toggle="modal"
                                        data-target="#modal-cerrar-sesion"
                                        >
                                        {{ $t('lang.cerrar-sesion') }}
                                    </a>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
        </div>

        <!-- MODAL CERRAR SESION -->
        <modal-confirmacion
            id="modal-cerrar-sesion"
            :contentModal="$t('lang.modal.sesion')"
            :textButtonDoAction="$t('lang.modal.confirmar')"
            :onClick="cerrarSesion"
        ></modal-confirmacion>
    </div>
</template>

<script>
import ModalConfirmacion from '@/components/modal/ModalConfirmacion.vue';
import { authStore } from '@/stores/auth';

export default {
    name: "MenuTop",
    components: {
        ModalConfirmacion
    },
    data() {
        return {
          auth: null,  
          user: null,
        }
    },
    created() {
        this.auth = authStore();
        this.user = this.auth.usuario;
    },
    methods: {
        cerrarSesion() {
            $("modal-cerrar-sesion").modal("hide");
            this.auth.cerrarSesion();
            this.$router.push("/auth")
        }
    }
}
</script>