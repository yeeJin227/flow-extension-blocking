'use client';

import { useState } from 'react';
import { CustomExtension } from '@/app/types/extension';

interface CustomExtensionInputProps {
  extensions: CustomExtension[];
  onAdd: (customExtensionName: string) => void;
  onDelete: (customExtensionName: string) => void;
}

export default function CustomExtensionInput({
  extensions,
  onAdd,
  onDelete,
}: CustomExtensionInputProps) {
  const [inputValue, setInputValue] = useState('');
  const MAX_LENGTH = 20;
  const MAX_COUNT = 200;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const trimmedValue = inputValue.trim().toLowerCase();

    if (!trimmedValue) {
      alert('확장자를 입력해주세요.');
      return;
    }

    // 중복 체크
    if (
      extensions.some(
        (ext) => ext.customExtensionName.toLowerCase() === trimmedValue
      )
    ) {
      alert('이미 존재하는 커스텀 확장자입니다.');
      setInputValue('');
      return;
    }

    // 최대 개수 체크
    if (extensions.length >= MAX_COUNT) {
      alert(`커스텀 확장자는 최대 ${MAX_COUNT}개까지 추가할 수 있습니다.`);
      setInputValue('');
      return;
    }

    onAdd(trimmedValue);
    setInputValue('');
  };

  return (
    <div>
      {/* 입력 폼 */}
      <form onSubmit={handleSubmit} className="mb-4">
        <div className="flex gap-2">
          <input
            type="text"
            value={inputValue}
            onChange={(e) => {
              const value = e.target.value;
              if (value.length <= MAX_LENGTH) {
                setInputValue(value);
              }
            }}
            placeholder="확장자 입력"
            maxLength={MAX_LENGTH}
            className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="submit"
            className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors font-medium"
          >
            추가
          </button>
        </div>
        <div className="mt-1 text-sm text-gray-500">
          {inputValue.length}/{MAX_LENGTH}자 (최대 {MAX_COUNT}개)
        </div>
      </form>

      {/* 확장자 목록 표시 */}
      <div className="min-h-[200px] border border-gray-300 rounded-lg p-4 bg-gray-50">
        {extensions.length === 0 ? (
          <div className="text-gray-400 text-center py-8">
            추가된 커스텀 확장자가 없습니다.
          </div>
        ) : (
          <div className="flex flex-wrap gap-2">
            {extensions.map((extension) => (
              <div
                key={extension.customExtensionName}
                className="flex items-center gap-2 px-3 py-2 bg-white border border-gray-300 rounded-lg shadow-sm"
              >
                <span className="text-gray-700 font-medium">
                  {extension.customExtensionName}
                </span>
                <button
                  onClick={() => onDelete(extension.customExtensionName)}
                  className="text-red-500 hover:text-red-700 font-bold text-lg leading-none"
                  aria-label="삭제"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
      <div className="mt-2 text-sm text-gray-500">
        현재 {extensions.length}/{MAX_COUNT}개
      </div>
    </div>
  );
}

