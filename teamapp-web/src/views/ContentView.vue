<template>
    <div class="flex_div navbar-top">
        <MenuTop></MenuTop>
        <div class="page-content">
            <MenuLeft></MenuLeft>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import MenuTop from '@/menu/MenuTop.vue';
import MenuLeft from '@/menu/MenuLeft.vue';

export default {
    components: {
        MenuTop,
        MenuLeft
    }
}

</script>
