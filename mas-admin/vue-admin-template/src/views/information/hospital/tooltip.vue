<template>
	<div>
		<el-tooltip :disabled="isShowTooltip" class="tooltip" placement="top">
			<pre class="tooltip__tip" slot="content">{{ messageWord }}</pre>
			<div class="tooltip__words" @mouseenter="enterEvents">{{ messageWord }}</div>
		</el-tooltip>
	</div>
</template>
<script>
export default {
	data() {
		return {
			messageWord: '',
			isShowTooltip: false
		}
	},
	props: {
		message: {
			required: true
		}
	},
	mounted() {
		this.messageWord = this.message
	},
	methods: {
		enterEvents(e) {
			let tableContentBoxWidth = e.target.getBoundingClientRect().width;
			let tableContentWidth = this.getElementTextWidth(e.target);
			if (tableContentWidth >= tableContentBoxWidth) {
				this.isShowTooltip = false;
			}else{
				this.isShowTooltip = true
			}
		},
		getElementTextWidth(el) {
			const range = new Range();
			range.selectNodeContents(el);
			const width = range.getBoundingClientRect().width;
			return width
		}
	}
}
</script>
<style scoped>
.tooltip__words {
	width: 100%;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.tooltip__tip {
	max-width: 500px;
	max-height: 300px;
	overflow-y: auto;
	white-space: pre-line;
}

.tooltip__tip::-webkit-scrollbar  {
	width: 6px;
}

.tooltip__tip::-webkit-scrollbar-thumb {
	background: #ccc; 
	border-radius: 3px; 
}

.tooltip__tip::-webkit-scrollbar-thumb:hover {
	background: #fff; 
}

.tooltip__tip::-webkit-scrollbar-track {
	border-radius: 3px; 
	background-color: #888 
}
</style>