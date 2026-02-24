import { defineStore } from 'pinia'
import { getSecondHandList, getSecondHandDetail, createSecondHandItem, updateSecondHandItem, deleteSecondHandItem, buySecondHandItem, getMyItems, addSecondHandFavorite, removeSecondHandFavorite } from '@/api/secondHand'

export const useSecondHandStore = defineStore('secondHand', {
  state: () => ({
    itemList: [
      {
        id: 1,
        title: '大学英语四级词汇书',
        price: 20,
        location: '东区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20for%20college%20students&image_size=square',
        description: '全新大学英语四级词汇书，附带光盘',
        category: '教材教辅',
        publishTime: '2026-02-10',
        condition: '全新'
      },
      {
        id: 2,
        title: '笔记本电脑',
        price: 3500,
        location: '西区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lenovo%20laptop%20computer%20for%20students&image_size=square',
        description: '联想小新Pro 13，九成新，配置良好',
        category: '电子产品',
        publishTime: '2026-02-09',
        condition: '九成新'
      },
      {
        id: 3,
        title: '篮球',
        price: 80,
        location: '南区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=basketball%20for%20students&image_size=square',
        description: '斯伯丁篮球，八成新，无损坏',
        category: '体育用品',
        publishTime: '2026-02-08',
        condition: '八成新'
      },
      {
        id: 4,
        title: '蓝牙耳机',
        price: 120,
        location: '北区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bluetooth%20earphones%20for%20students&image_size=square',
        description: '小米蓝牙耳机，九成新，音质良好',
        category: '电子产品',
        publishTime: '2026-02-07',
        condition: '九成新'
      },
      {
        id: 5,
        title: '考研政治复习资料',
        price: 50,
        location: '东区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=politics%20study%20materials%20for%20graduate%20entrance%20exam&image_size=square',
        description: '全套考研政治复习资料，几乎全新',
        category: '教材教辅',
        publishTime: '2026-02-06',
        condition: '九成新'
      },
      {
        id: 6,
        title: '电风扇',
        price: 30,
        location: '西区宿舍',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=electric%20fan%20for%20dormitory&image_size=square',
        description: '台式电风扇，使用一年，功能正常',
        category: '生活用品',
        publishTime: '2026-02-05',
        condition: '七成新'
      }
    ],
    itemDetail: {
      id: 1,
      title: '大学英语四级词汇书',
      price: 20,
      location: '东区宿舍',
      image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20for%20college%20students&image_size=square',
      images: [
        'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=english%20vocabulary%20book%20for%20college%20students&image_size=square',
        'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=book%20cover%20design%20for%20english%20learning&image_size=square'
      ],
      description: '<p>全新大学英语四级词汇书，附带光盘，适合备考四级的同学使用。</p><p>书中包含了四级考试必备的词汇，配有详细的释义和例句，帮助同学们更好地理解和记忆单词。</p><p>由于本人已经通过四级考试，所以这本书闲置了，希望能找到需要的同学。</p>',
      category: '教材教辅',
      publishTime: '2026-02-10',
      condition: '全新',
      purchaseTime: '2026-01-01',
      usageTime: '未使用',
      freeShipping: false,
      tradeMethod: '当面交易',
      tradeAddress: '东区宿舍楼下',
      seller: {
        id: 1,
        username: '小明',
        avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20friendly%20student&image_size=square',
        publishCount: 5
      }
    },
    myItems: [],
    favorites: [
      {
        id: 1,
        itemId: 2,
        itemTitle: '笔记本电脑',
        itemPrice: 3500,
        itemImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lenovo%20laptop%20computer%20for%20students&image_size=square',
        favoriteTime: '2026-02-10'
      }
    ],
    loading: false,
    total: 6,
    currentPage: 1,
    pageSize: 10
  }),

  getters: {
    itemCount: (state) => state.total,
    isFavorite: (state) => (itemId) => {
      return state.favorites.some(fav => fav.itemId === itemId)
    }
  },

  actions: {
    async getItemListAction(params) {
      this.loading = true
      try {
        // 生成缓存键
        const cacheKey = `secondHandList_${JSON.stringify(params)}`
        const cacheTimeKey = `${cacheKey}_time`
        
        // 检查本地缓存
        const cachedData = localStorage.getItem(cacheKey)
        const cachedTime = localStorage.getItem(cacheTimeKey)
        const now = Date.now()
        const cacheExpiry = 3 * 60 * 1000 // 3分钟缓存
        
        if (cachedData && cachedTime && (now - parseInt(cachedTime)) < cacheExpiry) {
          console.log('从缓存中获取二手交易列表数据')
          const parsedData = JSON.parse(cachedData)
          this.itemList = parsedData.list || []
          this.total = parsedData.total || 0
          this.currentPage = parsedData.currentPage || 1
          this.pageSize = parsedData.pageSize || 10
          this.loading = false
          return { data: parsedData }
        }
        
        // 从API获取数据
        const res = await getSecondHandList(params)
        this.itemList = res.data.list || []
        this.total = res.data.total || 0
        this.currentPage = res.data.currentPage || 1
        this.pageSize = res.data.pageSize || 10
        
        // 存入本地缓存
        const cacheData = {
          list: this.itemList,
          total: this.total,
          currentPage: this.currentPage,
          pageSize: this.pageSize
        }
        localStorage.setItem(cacheKey, JSON.stringify(cacheData))
        localStorage.setItem(cacheTimeKey, now.toString())
        console.log('二手交易列表数据已存入缓存')
        
        return res
      } catch (error) {
        // 模拟获取商品列表成功
        setTimeout(() => {
          let filteredList = [...this.itemList]
          
          // 模拟分类筛选
          if (params?.category) {
            filteredList = filteredList.filter(item => item.category === params.category)
          }
          
          // 模拟搜索
          if (params?.keyword) {
            const keyword = params.keyword.toLowerCase()
            filteredList = filteredList.filter(item => 
              item.title.toLowerCase().includes(keyword) || 
              item.description.toLowerCase().includes(keyword)
            )
          }
          
          this.itemList = filteredList
          this.total = filteredList.length
          this.currentPage = params?.page || 1
          this.pageSize = params?.pageSize || 10
          
          // 存入本地缓存
          const cacheKey = `secondHandList_${JSON.stringify(params)}`
          const cacheTimeKey = `${cacheKey}_time`
          const now = Date.now()
          const cacheData = {
            list: this.itemList,
            total: this.total,
            currentPage: this.currentPage,
            pageSize: this.pageSize
          }
          localStorage.setItem(cacheKey, JSON.stringify(cacheData))
          localStorage.setItem(cacheTimeKey, now.toString())
          console.log('二手交易列表模拟数据已存入缓存')
          
          this.loading = false
        }, 300)
        
        return {
          data: {
            list: this.itemList,
            total: this.total,
            currentPage: this.currentPage,
            pageSize: this.pageSize
          }
        }
      }
    },

    async getItemDetailAction(id) {
      this.loading = true
      try {
        const res = await getSecondHandDetail(id)
        this.itemDetail = res.data
        return res
      } catch (error) {
        // 模拟获取商品详情成功
        setTimeout(() => {
          const item = this.itemList.find(item => item.id === parseInt(id))
          if (item) {
            this.itemDetail = {
              ...item,
              images: [
                item.image,
                'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=book%20cover%20design%20for%20english%20learning&image_size=square'
              ],
              description: `<p>${item.description}</p><p>商品成色良好，欢迎有意者联系。</p>`,
              purchaseTime: '2026-01-01',
              usageTime: item.condition === '全新' ? '未使用' : '使用半年',
              freeShipping: false,
              tradeMethod: '当面交易',
              tradeAddress: item.location + '楼下',
              seller: {
                id: 1,
                username: '小明',
                avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20friendly%20student&image_size=square',
                publishCount: 5
              }
            }
          }
          this.loading = false
        }, 300)
        
        return {
          data: this.itemDetail
        }
      }
    },

    async createItemAction(data) {
      try {
        const res = await createSecondHandItem(data)
        return res
      } catch (error) {
        // 模拟创建商品成功
        setTimeout(() => {
          const newItem = {
            id: Date.now(),
            ...data,
            publishTime: new Date().toISOString().split('T')[0],
            image: data.image || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=second%20hand%20item&image_size=square'
          }
          this.itemList.unshift(newItem)
          this.total++
        }, 300)
        
        return {
          data: { success: true }
        }
      }
    },

    async updateItemAction(id, data) {
      try {
        const res = await updateSecondHandItem(id, data)
        return res
      } catch (error) {
        // 模拟更新商品成功
        setTimeout(() => {
          const index = this.itemList.findIndex(item => item.id === parseInt(id))
          if (index !== -1) {
            this.itemList[index] = { ...this.itemList[index], ...data }
          }
        }, 300)
        
        return {
          data: { success: true }
        }
      }
    },

    async deleteItemAction(id) {
      try {
        const res = await deleteSecondHandItem(id)
        return res
      } catch (error) {
        // 模拟删除商品成功
        setTimeout(() => {
          this.itemList = this.itemList.filter(item => item.id !== parseInt(id))
          this.total--
        }, 300)
        
        return {
          data: { success: true }
        }
      }
    },

    async buyItemAction(id) {
      try {
        const res = await buySecondHandItem(id)
        return res
      } catch (error) {
        // 模拟购买商品成功
        setTimeout(() => {
          const index = this.itemList.findIndex(item => item.id === parseInt(id))
          if (index !== -1) {
            this.itemList[index].status = 'sold'
          }
        }, 300)
        
        return {
          data: { success: true, message: '购买成功' }
        }
      }
    },

    async getMyItemsAction(params) {
      this.loading = true
      try {
        const res = await getMyItems(params)
        this.myItems = res.data.list || []
        this.total = res.data.total || 0
        return res
      } catch (error) {
        // 模拟获取我的商品成功
        setTimeout(() => {
          // 模拟筛选
          if (params?.status === 'published') {
            this.myItems = this.itemList.slice(0, 2)
          } else {
            this.myItems = []
          }
          this.total = this.myItems.length
          this.loading = false
        }, 300)
        
        return {
          data: {
            list: this.myItems,
            total: this.total
          }
        }
      }
    },

    // 添加收藏
    async addFavorite(item) {
      await addSecondHandFavorite(item.id)
      const newFavorite = {
        id: Date.now(),
        itemId: item.id,
        itemTitle: item.title,
        itemPrice: item.price,
        itemImage: item.image,
        favoriteTime: new Date().toISOString().split('T')[0]
      }
      this.favorites.push(newFavorite)
      return { data: { success: true, message: '收藏成功' } }
    },

    // 取消收藏
    async removeFavorite(itemId) {
      await removeSecondHandFavorite(itemId)
      this.favorites = this.favorites.filter(fav => fav.itemId !== parseInt(itemId))
      return { data: { success: true, message: '取消收藏成功' } }
    },

    // 获取收藏列表
    async getFavorites() {
      return { data: this.favorites }
    },

    resetItemList() {
      this.itemList = []
      this.total = 0
      this.currentPage = 1
    },

    resetItemDetail() {
      this.itemDetail = null
    },

    clearCache() {
      this.itemList = []
      this.itemDetail = null
      this.myItems = []
      this.favorites = []
      this.total = 0
      this.currentPage = 1
      this.pageSize = 10
      console.log('[SecondHandStore] 已清除所有缓存')
    }
  }
})
