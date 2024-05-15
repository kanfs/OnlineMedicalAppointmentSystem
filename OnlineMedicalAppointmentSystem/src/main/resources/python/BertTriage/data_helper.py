#! -*- coding: utf-8 -*-
import json
import random
import os
import jieba
import pandas as pd 

def gen_training_data(raw_data_path):
    label_list = [line.strip().split("\t")[1] for line in open('./data/label.txt','r',encoding='utf-8')]
    print(label_list)
    label2id = {label:idx for idx,label in enumerate(label_list)}

    data = []
    with open(raw_data_path,'r',encoding='utf-8') as f:
        origin_data = f.readlines()

    label_set = set()
    for line in origin_data:
        item = json.loads(line)
        text = item["question"]
        label_class = item["label"]
        if label_class == "急诊科" or label_class=="其他":
            continue
        data.append([text,label2id[label_class]])
        label_set.add(label_class)

    print(label_set)

    return data

def load_data(filename):
    """加载数据
    单条格式：(文本, 标签id)
    """
    df = pd.read_csv(filename,header=0)
    return df[['text','label']].values

if __name__ == '__main__':
    
    data_path = "./data/format_data.jsonl"
    data = gen_training_data(data_path)

    data = pd.DataFrame(data,columns=['text','label'])
    data = data.sample(frac=1.0)
    print(data['label'].value_counts())

    data['text_len'] = data['text'].map(lambda x: len(x))
    print(data['text_len'].describe())
    import matplotlib.pyplot as plt
    plt.hist(data['text_len'], bins=30, rwidth=0.9, density=True,)
    plt.show()

    del data['text_len']

    train_num = int(0.9*len(data))
    train,test = data[:train_num],data[train_num:]
    train.to_csv("./data/train.csv",index=False)
    test.to_csv("./data/test.csv",index=False)
