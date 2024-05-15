
if __name__ == '__main__':
    cookie = "g=96780_1700813193537; krandom_a119fcaa84=252316; HMF_CI=60e2ab0340aa2f41ea5f33662cf3feec273b480d3740ff331bbb10ee82aa4f35e9e1286aa597b978d8ce6f07f50b593fb816645b263f6896ff1c9c6fdcde8b6cdd; Hm_lvt_dfa5478034171cc641b1639b2a5b717d=1700813206; userinfo[id]=10735407907; userinfo[time]=1700813252; userinfo[hostid]=0; userinfo[key]=A35RYFdlB2BUPVBiBTMBYwduUmcAMAFlBC0Ea1RmAWpRZgxhADIDawY4An1VMwIjUHJVaQAp; userinfo[ver]=1.0.2; userinfo[name]=hdfbm08dxds; sdmsg=1; aliyungf_tc=fe1809c2e21e535e71098684fb89964e55fa9eb62a9f1cad11d217d37e0f2e55; acw_tc=c0a8050117008177054844539eaeb4dccf9a21520e704b0a45e642a7453bfe; Hm_lpvt_dfa5478034171cc641b1639b2a5b717d=1700817710; SERVERID=f681c18800e34eaeef5aef81453a4655|1700817870|1700817705"
    item_dict = {}
    items = cookie.split(";")
    for item in items:
        key = item.split("=")[0].strip()
        value = item.split("=")[1]
        item_dict[key] = value
    print(item_dict)