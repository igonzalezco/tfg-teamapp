<template>
    <div class="sidebar sidebar-light sidebar-main sidebar-fixed sidebar-expand-md">
        <div class="sidebar-content">
            <!-- Menú equipos -->
        </div>

        <el-menu
          :default-active="activeIndex"
          mode="vertical"
          active-text-color="#ffd04b"
        >
            <el-sub-menu index="1">
               <template #title>Nivel 1</template>
               <el-menu-item index="1-1">Nivel 1-1</el-menu-item>
            </el-sub-menu>

        </el-menu>
    </div>
</template>

<script>
import { ref } from 'vue'


export default {
    name: "MenuLeft",
    setup() {
        const activeIndex = ref('1')

        return {
            activeIndex
        }
    }
}

</script>